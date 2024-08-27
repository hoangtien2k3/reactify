package io.hoangtien2k3.keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SpringBootApplication
public class KeycloakApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeycloakApplication.class, args);
	}

	Map<String, Object> parentValues = new HashMap<>();
	List<String> failedParentIds = new ArrayList<>();

try {
		parentValues = listSubOrder.stream()
				.map(Orders::getParentBpId)
				.distinct()
				.filter(Objects::nonNull)
				.collect(Collectors.toMap(
						parentBpId -> parentBpId,
						parentBpId -> {
							try {
								return runtineService.getVariable(parentBpId, finalNameVariable);
							} catch (Exception e) {
								// Nếu có lỗi khi lấy biến, thêm parentBpId vào danh sách lỗi
								failedParentIds.add(parentBpId);
								return null; // Giá trị null sẽ bị filter sau đó
							}
						}
				));

		// Lọc bỏ các entry có giá trị null (tức là những đơn hàng cha không hoạt động)
		parentValues.entrySet().removeIf(entry -> entry.getValue() == null);

		// Nếu có bất kỳ parentBpId nào không lấy được biến, ném lỗi tại đây
		if (!failedParentIds.isEmpty()) {
			String failedIds = String.join(", ", failedParentIds);
			Logger.error("Failed to retrieve variables for parent BpIds: {}", failedIds);
			throw new LogicException("SUBOR00004", "value.name.variable.nut?");
		}

	} catch (Exception e) {
		Logger.error("An unexpected error occurred: {}", e.getMessage(), e);
		throw new LogicException("SUBOR00004", "value.name.variable.nut?");
	}

}
