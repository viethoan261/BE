package hoan.com.springboot.services.impl;

import hoan.com.springboot.common.enums.Gender;
import hoan.com.springboot.common.error.BadRequestError;
import hoan.com.springboot.common.exception.ResponseException;
import hoan.com.springboot.mapper.UserMapper;
import hoan.com.springboot.models.entities.RoleEntity;
import hoan.com.springboot.models.entities.UserEntity;
import hoan.com.springboot.models.entities.UserRoleEntity;
import hoan.com.springboot.payload.request.UserUpdateRequest;
import hoan.com.springboot.payload.response.UserResponse;
import hoan.com.springboot.repository.RoleRepository;
import hoan.com.springboot.repository.UserRepository;
import hoan.com.springboot.repository.UserRoleRepository;
import hoan.com.springboot.services.UserService;
import hoan.com.springboot.utils.ExcelUtils;
import hoan.com.springboot.utils.StringPool;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final RoleRepository roleRepository;

    private static final String TEMPLATE_USER_FILE_PATH = "templates/excel/Template_USER.xlsx";
    private static final String TEMPLATE_USER_FILE_NAME = "Danh_sach_nhan_vien";
    private static final int TEMPLATE_USER_SHEET_NUMBER = 0;
    private static final int USER_START_ROW = 2;

    @Override
    public List<UserResponse> findAll() {
        List<UserResponse> res = UserMapper.INSTANCE.from(userRepository.findAll());

        for (UserResponse user : res) {
            List<UserRoleEntity> userRoleEntities = userRoleRepository.findAllByUserId(user.getId());

            List<UUID> roleIds = userRoleEntities.stream().map(UserRoleEntity::getRoleId).collect(Collectors.toList());

            List<RoleEntity> roles = roleRepository.findAllByIds(roleIds);

            user.setRoles(roles);
        }

        return res;
    }

    @Override
    @Transactional
    public UserResponse update(String id, UserUpdateRequest request) {
        UUID userId = UUID.fromString(id);

        if (checkDuplicateEmail(request.getEmail(), userId)) {
            throw new ResponseException(BadRequestError.EMAIL_INVALID);
        }

        if (checkDuplicatePhone(request.getPhoneNumber(), userId)) {
            throw new ResponseException(BadRequestError.PHONE_INVALID);
        }

        Optional<UserEntity> userOtp = userRepository.findById(userId);

        if (userOtp.isEmpty()) {
            throw new ResponseException(BadRequestError.USER_NOT_FOUND);
        }

        UserEntity userUpdate = userOtp.get();

        userUpdate = UserMapper.INSTANCE.from(request, userUpdate);

        //save userUpdate
        userRepository.save(userUpdate);

        //save user role if changes
        List<UserRoleEntity> userRoleEntities = userRoleRepository.findAllByUserId(userId);

        //delete old user role
        userRoleRepository.deleteAll(userRoleEntities);

        List<UUID> roleIds = request.getRoleIds().stream().map(UUID::fromString).collect(Collectors.toList());

        List<UserRoleEntity> rolesUpdate = new ArrayList<>();

        for (UUID roleId : roleIds) {
            UserRoleEntity userRoleEntity = new UserRoleEntity();

            userRoleEntity.setUserId(userId);
            userRoleEntity.setRoleId(roleId);

            rolesUpdate.add(userRoleEntity);
        }

        userRoleRepository.saveAll(rolesUpdate);

        UserResponse res;

        List<RoleEntity> roles = roleRepository.findAllByIds(roleIds);

        res = UserMapper.INSTANCE.from(userUpdate);
        res.setRoles(roles);

        return res;
    }

    @Override
    public UserResponse getById(String id) {
        UUID userId = UUID.fromString(id);

        Optional<UserEntity> userOtp = userRepository.findById(userId);

        if (userOtp.isEmpty()) {
            throw new ResponseException(BadRequestError.USER_NOT_FOUND);
        }

        UserResponse res;

        List<UserRoleEntity> userRoles = userRoleRepository.findAllByUserId(userId);

        List<UUID> ids = userRoles.stream().map(UserRoleEntity::getRoleId).collect(Collectors.toList());

        List<RoleEntity> roles = roleRepository.findAllByIds(ids);

        res = UserMapper.INSTANCE.from(userOtp.get());
        res.setRoles(roles);

        return res;
    }

    @Override
    public void exportToExcel(HttpServletResponse response) {
        InputStream data = Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(TEMPLATE_USER_FILE_PATH));

        List<UserEntity> users = userRepository.findAll();

        XSSFWorkbook workbook;

        try {
            workbook = new XSSFWorkbook(data);
        } catch (IOException e) {
            throw new ResponseException(BadRequestError.OPEN_USER_TEMPLATE_FAIL);
        }

        Sheet userSheet = workbook.getSheetAt(TEMPLATE_USER_SHEET_NUMBER);

        if (Objects.isNull(userSheet)) {
            throw new ResponseException(BadRequestError.USER_SHEET_TEMPLATE_REQUIRED);
        }

        for (int i = 0; i < users.size(); i++) {
            UserEntity user = users.get(i);

            Row currentRow = userSheet.createRow(USER_START_ROW + i);

            CellStyle cellStyle = ExcelUtils.createValueCellStyle(workbook);

            ExcelUtils.createCell(currentRow, 0, cellStyle, String.valueOf(i + 1));
            ExcelUtils.createCell(currentRow, 1, cellStyle, user.getEmployeeCode());
            ExcelUtils.createCell(currentRow, 2, cellStyle, user.getFullName());
            ExcelUtils.createCell(currentRow, 3, cellStyle, user.getDayOfBirth().toString());
            ExcelUtils.createCell(currentRow, 4, cellStyle, user.getGender().equals(Gender.MALE) ? "Nam" : "Nữ");
            ExcelUtils.createCell(currentRow, 5, cellStyle, user.getPhoneNumber());
            ExcelUtils.createCell(currentRow, 6, cellStyle, user.getEmail());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            String filename = TEMPLATE_USER_FILE_NAME + StringPool.UNDERLINE + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +
                    StringPool.XLSX;
            workbook.write(out);
            response.setHeader("Content-Type", "application/octet-stream");
            response.setHeader("Content-Disposition", " attachment; filename=" + filename);
            IOUtils.copy(new ByteArrayInputStream(out.toByteArray()), response.getOutputStream());
            response.flushBuffer();
            out.close();
            workbook.close();
        } catch (IOException e) {
            throw new ResponseException(BadRequestError.DOWNLOAD_USER_TEMPLATE_FAIL);

        }
    }

    private boolean checkDuplicateEmail(String email, UUID id) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);

        return userEntity.filter(entity -> !entity.getId().equals(id)).isPresent();

    }

    private boolean checkDuplicatePhone(String phone, UUID id) {
        Optional<UserEntity> userEntity = userRepository.findByPhoneNumber(phone);

        return userEntity.filter(entity -> !entity.getId().equals(id)).isPresent();

    }
}
