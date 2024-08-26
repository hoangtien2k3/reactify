package io.hoangtien2k3.keycloak.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeCreateRequest {
    private FileDTO image;
    private String name;
    private String phone;
    private String email;
    private LocalDateTime birthday;
    private String gender;
    private String code;
    private Integer status;
    private String address;
    private List<EmployeePositionRequest> employeePositionRequestList;
    private LocalDateTime probationDay;
    private LocalDateTime startWorkingDay;
    private String username;
    private String emailAccount;
    private Boolean sendEmail;
    private Integer accountStatus;
    private List<EmployeePermissionRequest> employeePermissionRequestList;
    private Boolean isEditable;
}
