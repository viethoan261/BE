package hoan.com.springboot.services.impl;

import hoan.com.springboot.common.UserAuthority;
import hoan.com.springboot.common.enums.EventHistory;
import hoan.com.springboot.common.enums.TimeOffType;
import hoan.com.springboot.common.error.AuthenticationError;
import hoan.com.springboot.common.error.BadRequestError;
import hoan.com.springboot.common.exception.ResponseException;
import hoan.com.springboot.common.util.GenerateCodeHelper;
import hoan.com.springboot.mapper.UserMapper;
import hoan.com.springboot.models.entities.*;
import hoan.com.springboot.payload.request.*;
import hoan.com.springboot.payload.response.UserResponse;
import hoan.com.springboot.repository.*;
import hoan.com.springboot.security.jwt.JwtUtils;
import hoan.com.springboot.security.services.AuthorityService;
import hoan.com.springboot.security.utils.SecurityUtils;
import hoan.com.springboot.services.AccountService;
import hoan.com.springboot.utils.TimeOffConst;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    private final PasswordEncoder encoder;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityService authorityService;

    private final JwtUtils jwts;

    private final TimeOffRepository timeOffRepository;

    private final TimeOffHistoryRepository timeOffHistoryRepository;

    private final UserDetailsService userDetailsService;

    private final SessionRegistry sessionRegistry;

    @Transactional
    @Override
    public UserEntity register(UserRegisterRequest request) {

        List<String> roleIds = request.getRoleIds();
        List<UserRoleEntity> userRoles = new ArrayList<>();

        if (!this.checkRoleExisted(roleIds)) {
            throw new ResponseException(BadRequestError.INVALID_ROLE);
        }

        UserEntity newUser = new UserEntity();

        String encodedPassword = this.passwordEncoder.encode(request.getPassword());

        newUser.setEmail(request.getEmail());
        newUser.setUsername(request.getUsername().toLowerCase());
        newUser.setPassword(encodedPassword);
        newUser.setFullName(request.getFullName());
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setGender(request.getGender());
        newUser.setDescription(request.getDescription());
        newUser.setEmployeeCode(GenerateCodeHelper.generateEmployeeCode());
        newUser.setAvatarFileId(Objects.isNull(request.getAvatar()) ? null : request.getAvatar());
        newUser.setDayOfBirth(Objects.isNull(request.getDayOfBirth()) ? null : request.getDayOfBirth());
        newUser.setDepartmentId(request.getDepartmentId() != null ? UUID.fromString(request.getDepartmentId()) : null);

        //save
        this.userRepository.save(newUser);

        //save user role
        for (String roleId : roleIds
        ) {
            UserRoleEntity userRole = new UserRoleEntity();

            userRole.setRoleId(UUID.fromString(roleId));
            userRole.setUserId(newUser.getId());

            userRoles.add(userRole);
        }

        userRoleRepository.saveAll(userRoles);

        //update leave day
        List<TimeOffEntity> timeOffEntities = this.updateLeaveDay(newUser.getId());

        //update history balance
        this.updateBalance(timeOffEntities);

        return newUser;
    }

    private List<TimeOffEntity> updateLeaveDay(UUID employeeId) {
        List<TimeOffEntity> timeOffEntities = new ArrayList<>();

        for (TimeOffType type : TimeOffType.values()) {
            TimeOffEntity timeOffEntity = new TimeOffEntity();

            timeOffEntity.setEmployeeId(employeeId);
            timeOffEntity.setType(type);

            switch (type) {
                case ANNUAL:
                    LocalDate currentDate = LocalDate.now();

                    // Lấy giá trị enum ANNUAL
                    float totalLeaveDays = TimeOffConst.ANNUAL;

                    // Tính khoảng thời gian giữa thời gian hiện tại và thời gian sử dụng phép trước đó
//                    Period period = Period.between(previousTimeOffDate, currentDate);
//                    float daysSincePreviousTimeOff = period.getDays();
                    int month = currentDate.getMonthValue();
                    timeOffEntity.setTotal(totalLeaveDays - month + 1);

                    timeOffRepository.save(timeOffEntity);

                    timeOffEntities.add(timeOffEntity);

                    break;
                case UNPAID_TIME_OFF:
                    timeOffEntity.setTotal(Float.valueOf(TimeOffConst.UNPAID_TIME_OFF));

                    timeOffRepository.save(timeOffEntity);

                    timeOffEntities.add(timeOffEntity);

                    break;
                case WEDDING:
                    timeOffEntity.setTotal(Float.valueOf(TimeOffConst.WEDDING));

                    timeOffRepository.save(timeOffEntity);

                    timeOffEntities.add(timeOffEntity);

                    break;
            }
        }

        return timeOffEntities;
    }

    private void updateBalance(List<TimeOffEntity> timeOffEntities) {
        List<TimeOffHistoryEntity> timeOffHistoryEntities = new ArrayList<>();

        if (Objects.isNull(timeOffEntities)) {
            throw new ResponseException(BadRequestError.NULL_POINTER);
        }

        String loginId = SecurityUtils.getCurrentUserLoginId().get();

        UserEntity user = userRepository.findById(UUID.fromString(loginId)).get();

        for (TimeOffEntity timeOffEntity : timeOffEntities) {
            TimeOffHistoryEntity timeOffHistoryEntity = new TimeOffHistoryEntity();

            timeOffHistoryEntity.setType(timeOffEntity.getType());
            timeOffHistoryEntity.setEvent(EventHistory.ACCRUAL);
            timeOffHistoryEntity.setEmployeeId(timeOffEntity.getEmployeeId());
            timeOffHistoryEntity.setChangeDays(timeOffEntity.getTotal());
            timeOffHistoryEntity.setChangedBy(user.getFullName());
            timeOffHistoryEntity.setDate(LocalDate.now());

            timeOffHistoryEntities.add(timeOffHistoryEntity);
        }

        timeOffHistoryRepository.saveAll(timeOffHistoryEntities);
    }

    @Override
    public String login(LoginRequest request) {
        // get user info
        Optional<UserEntity> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            throw new ResponseException(BadRequestError.USER_NOT_FOUND);
        }

        //check deleted
        if (userOpt.get().getDeleted()) {
            throw new ResponseException(BadRequestError.USER_NOT_ACTIVE);
        }

        // check password
        String encodedPassword = userOpt.get().getPassword();

        if (passwordEncoder.matches(request.getPassword(), encodedPassword)) {
            // Lấy thông tin người dùng
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

            // Tạo đối tượng Authentication từ UserDetails
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            sessionRegistry.registerNewSession(userDetails.getUsername(), authentication.getPrincipal());

            return jwts.generateJwtToken(request.getUsername());
        } else {
            throw new ResponseException(BadRequestError.LOGIN_FAIL_DUE_INACTIVE_ACCOUNT);
        }
    }

    @Override
    public UserAuthority myAuthorities() {
        String me = currentUserId();

        return this.authorityService.getUserAuthority(me);
    }

    @Override
    public UserResponse getProfile() {
        UserResponse res;

        String userId = currentUserId();

        UserEntity user = userRepository.findById(UUID.fromString(userId)).get();

        List<UserRoleEntity> roleUsers = userRoleRepository.findAllByUserId(user.getId());

        List<UUID> roleIds = roleUsers.stream().map(UserRoleEntity::getRoleId).collect(Collectors.toList());

        List<RoleEntity> roles = roleRepository.findAllByIds(roleIds);

        res = UserMapper.INSTANCE.from(user);
        res.setRoles(roles);

        return res;
    }

    @Override
    @Transactional
    public boolean changePwd(ChangePwdRequest request) {
        String userId = currentUserId();

        UserEntity user = userRepository.findById(UUID.fromString(userId)).get();

        String pwdUpdate = passwordEncoder.encode(request.getPassword());

        user.setPassword(pwdUpdate);

        userRepository.save(user);

        return true;
    }

    @Override
    public boolean logout() {
        String userId = currentUserId();

        UserEntity user = userRepository.findById(UUID.fromString(userId)).get();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        // Tạo đối tượng Authentication từ UserDetails
        List<Object> principals = sessionRegistry.getAllPrincipals();

        for (Object principal : principals) {
            if (principal instanceof UserDetails) {
                UserDetails loggedInUser = (UserDetails) principal;

                if (loggedInUser.getUsername().equals(user.getUsername())) {
                    List<SessionInformation> sessionInfoList = sessionRegistry.getAllSessions(principal, false);

                    for (SessionInformation sessionInformation : sessionInfoList) {
                        // Đánh dấu phiên đã kết thúc
                        sessionInformation.expireNow();

                        sessionRegistry.removeSessionInformation(sessionInformation.getSessionId());
                    }
                    break;
                }
            }
        }

        return true;
    }

    private boolean checkRoleExisted(List<String> roleIds) {
        List<UUID> ids = roleIds.stream().map(UUID::fromString).collect(Collectors.toList());

        List<RoleEntity> roles = roleRepository.findAllByIds(ids);

        return roles.size() == roleIds.size();
    }

    private String currentUserId() {
        Optional<String> currentUserLoginId = SecurityUtils.getCurrentUserLoginId();
        if (currentUserLoginId.isEmpty()) {
            throw new ResponseException(AuthenticationError.UNAUTHORISED);
        }
        return currentUserLoginId.get();
    }
}
