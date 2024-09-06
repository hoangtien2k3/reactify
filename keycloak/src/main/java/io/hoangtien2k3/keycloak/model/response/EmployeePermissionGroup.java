package io.hoangtien2k3.keycloak.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeePermissionGroup {
    private String groupId;
    private String groupCode;
    private String policyId;
}
