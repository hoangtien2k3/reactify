package io.hoangtien2k3.commons.filter.properties;

import io.hoangtien2k3.commons.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ClientLogProperties {
    private boolean enable = true;
    private List<String> obfuscateHeaders = Constants.getSensitiveHeaders();
}
