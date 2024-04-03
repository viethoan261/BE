package hoan.com.springboot.services.impl;

import hoan.com.springboot.common.enums.Gender;
import hoan.com.springboot.common.error.BadRequestError;
import hoan.com.springboot.common.exception.ResponseException;
import hoan.com.springboot.common.util.GenerateCodeHelper;
import hoan.com.springboot.mapper.DepartmentMapper;
import hoan.com.springboot.models.entities.DepartmentEntity;
import hoan.com.springboot.models.entities.UserEntity;
import hoan.com.springboot.payload.request.DepartmentCreateOrUpdateRequest;
import hoan.com.springboot.payload.response.DepartmentResponse;
import hoan.com.springboot.repository.DepartmentRepository;
import hoan.com.springboot.repository.UserRepository;
import hoan.com.springboot.services.DepartmentService;
import hoan.com.springboot.utils.ExcelUtils;
import hoan.com.springboot.utils.StringPool;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final UserRepository userRepository;

    private static final String TEMPLATE_DEPARTMENT_FILE_PATH = "templates/excel/Template_DEPARTMENT.xlsx";
    private static final String TEMPLATE_DEPARTMENT_FILE_NAME = "Danh_sach_phong_ban";
    private static final int TEMPLATE_DEPARTMENT_SHEET_NUMBER = 0;
    private static final int DEPARTMENT_START_ROW = 2;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DepartmentEntity create(DepartmentCreateOrUpdateRequest request) {
        DepartmentEntity departmentEntity = new DepartmentEntity();

        departmentEntity.setCode(GenerateCodeHelper.generateDepartmentCode());
        departmentEntity.setName(request.getName());
        departmentEntity.setDescription(request.getDescription());

        if (!Objects.isNull(request.getParentId())) {
            UUID parentID = UUID.fromString(request.getParentId());

            Optional<DepartmentEntity> departmentParent = departmentRepository.findById(parentID);

            if (departmentParent.isEmpty()) {
                throw new ResponseException(BadRequestError.DEPARTMENT_NOT_EXISTED);
            }

            departmentEntity.setDepartmentPath(departmentParent.get().getDepartmentPath() + "/" + request.getName());
            departmentEntity.setParentId(parentID);
        } else {
            departmentEntity.setDepartmentPath(request.getName());
        }

        departmentRepository.save(departmentEntity);

        return departmentEntity;
    }

    @Override
    public DepartmentEntity update(String id, DepartmentCreateOrUpdateRequest request) {
        UUID parentID = UUID.fromString(request.getParentId());

        DepartmentEntity department = this.getDepartmentById(id);

        department.setDescription(request.getDescription());
        department.setName(request.getName());

        if (Objects.nonNull(request.getParentId())) {

            Optional<DepartmentEntity> departmentParent = departmentRepository.findById(parentID);

            if (departmentParent.isEmpty()) {
                throw new ResponseException(BadRequestError.DEPARTMENT_NOT_EXISTED);
            }

            department.setDepartmentPath(departmentParent.get().getDepartmentPath() + "/" + request.getName());
            department.setParentId(parentID);
        } else {
            department.setDepartmentPath(request.getName());
        }

        departmentRepository.save(department);

        return department;
    }

    @Override
    public boolean delete(String id) {
        DepartmentEntity department = this.getDepartmentById(id);

        department.setDeleted(Boolean.TRUE);

        departmentRepository.save(department);

        return true;
    }

    @Override
    public List<DepartmentResponse> getAll() {
        List<DepartmentEntity> departmentEntities = departmentRepository.findAll();

        List<DepartmentResponse> res = DepartmentMapper.INSTANCE.from(departmentEntities);

        for (DepartmentResponse departmentResponse : res) {
            List<UserEntity> users = userRepository.findAllByDepartment(departmentResponse.getId());

            departmentResponse.setUsers(users);
        }

        return res;
    }

    @Override
    public DepartmentResponse detail(String id) {
        DepartmentEntity department = this.getDepartmentById(id);

        List<UserEntity> users = userRepository.findAllByDepartment(UUID.fromString(id));

        DepartmentResponse res = DepartmentMapper.INSTANCE.from(department);
        res.setUsers(users);

        return res;
    }

    @Override
    public boolean deleteUser(String departmentId, String userId) {
        Optional<DepartmentEntity> departmentOpt = departmentRepository.findById(UUID.fromString(departmentId));

        if (departmentOpt.isEmpty()) {
            throw new ResponseException(BadRequestError.DEPARTMENT_NOT_EXISTED);
        }

        Optional<UserEntity> userOpt = userRepository.findById(UUID.fromString(userId));

        if (userOpt.isEmpty()) {
            throw new ResponseException(BadRequestError.USER_NOT_FOUND);
        }

        UserEntity user = userOpt.get();
        user.setDepartmentId(null);

        userRepository.save(user);

        return true;
    }

    @Override
    public boolean addUser(String departmentId, List<String> userIds) {
        DepartmentEntity department = this.getDepartmentById(departmentId);

        List<UUID> userIdsConvert = userIds.stream().map(UUID::fromString).collect(Collectors.toList());

        List<UserEntity> users = userRepository.findByIds(userIdsConvert);

        if (users.size() != userIds.size()) {
            throw new ResponseException(BadRequestError.USER_NOT_FOUND);
        }

        for (UserEntity user : users) {
            user.setDepartmentId(UUID.fromString(departmentId));
        }

        userRepository.saveAll(users);

        return true;
    }

    @Override
    public void exportToExcel(HttpServletResponse response) {
        InputStream data = Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(TEMPLATE_DEPARTMENT_FILE_PATH));

        List<DepartmentEntity> departmentEntities = departmentRepository.findAll();

        XSSFWorkbook workbook;

        try {
            workbook = new XSSFWorkbook(data);
        } catch (IOException e) {
            throw new ResponseException(BadRequestError.OPEN_DEPARTMENT_TEMPLATE_FAIL);
        }

        Sheet departmentSheet = workbook.getSheetAt(TEMPLATE_DEPARTMENT_SHEET_NUMBER);

        if (Objects.isNull(departmentSheet)) {
            throw new ResponseException(BadRequestError.DEPARTMENT_SHEET_TEMPLATE_REQUIRED);
        }

        for (int i = 0; i < departmentEntities.size(); i++) {
            DepartmentEntity department = departmentEntities.get(i);

            Row currentRow = departmentSheet.createRow(DEPARTMENT_START_ROW + i);

            CellStyle cellStyle = ExcelUtils.createValueCellStyle(workbook);

            ExcelUtils.createCell(currentRow, 0, cellStyle, String.valueOf(i + 1));
            ExcelUtils.createCell(currentRow, 1, cellStyle, department.getCode());
            ExcelUtils.createCell(currentRow, 2, cellStyle, department.getName());
            ExcelUtils.createCell(currentRow, 3, cellStyle, department.getDescription() == null ? "N/A" : department.getDescription());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            String filename = TEMPLATE_DEPARTMENT_FILE_NAME + StringPool.UNDERLINE + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +
                    StringPool.XLSX;
            workbook.write(out);
            response.setHeader("Content-Type", "application/octet-stream");
            response.setHeader("Content-Disposition", " attachment; filename=" + filename);
            IOUtils.copy(new ByteArrayInputStream(out.toByteArray()), response.getOutputStream());
            response.flushBuffer();
            out.close();
            workbook.close();
        } catch (IOException e) {
            throw new ResponseException(BadRequestError.DOWNLOAD_TEMPLATE_TEMPLATE_FAIL);

        }
    }

    private DepartmentEntity getDepartmentById(String id) {
        Optional<DepartmentEntity> departmentOpt = departmentRepository.findById(UUID.fromString(id));

        if (departmentOpt.isEmpty()) {
            throw new ResponseException(BadRequestError.DEPARTMENT_NOT_EXISTED);
        }

        return departmentOpt.get();
    }
}
