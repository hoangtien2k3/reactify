/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.hoangtien2k3.utils;

/**
 * Utility class for pagination-related operations.
 * <p>
 * The {@code PageUtils} class provides a static method to calculate the offset
 * for pagination based on the current page number and page size. This is
 * particularly useful for retrieving paginated results from a database or other
 * data sources.
 * </p>
 *
 * <h2>Class Overview:</h2>
 * <p>
 * This class contains utility methods for handling pagination calculations.
 * Currently, it includes a method to compute the offset given a page number and
 * page size.
 * </p>
 *
 * <h2>Methods:</h2>
 * <ul>
 * <li><strong>getOffset</strong>: Calculates the offset for pagination based on
 * the provided page number and page size.
 * <ul>
 * <li><strong>Parameters:</strong>
 * <ul>
 * <li><code>page</code> (Integer): The current page number. Must be greater
 * than 0.</li>
 * <li><code>size</code> (Integer): The number of items per page. Must be
 * greater than 0.</li>
 * </ul>
 * </li>
 * <li><strong>Returns:</strong>
 * <p>
 * An integer representing the offset. If the <code>page</code> or
 * <code>size</code> is null or less than or equal to 0, the method returns 0.
 * </p>
 * </li>
 * <li><strong>Usage:</strong>
 * <p>
 * This method can be used to calculate the starting index for a particular page
 * in a paginated data set. For example, if the page size is 10 and the current
 * page is 3, the method will calculate an offset of 20, which is the starting
 * index for the 3rd page (page index starts at 0).
 * </p>
 * </li>
 * </ul>
 * </li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>{@code
 * int page = 3;
 * int size = 10;
 * int offset = PageUtils.getOffset(page, size);
 * // offset will be 20, which is used to fetch records starting from index 20
 * }</pre>
 *
 * <h2>Notes:</h2>
 * <p>
 * Ensure that the page number and page size are validated before calling the
 * method. If either value is invalid (e.g., less than or equal to 0), the
 * method returns 0, which might result in incorrect pagination behavior if not
 * handled properly in the application logic.
 * </p>
 */
public class PageUtils {

    /**
     * Calculates the offset for pagination based on the current page number and
     * page size.
     * <p>
     * This method computes the starting index for the given page number and page
     * size. The offset is calculated as (page - 1) * size. If the page or size is
     * null or not positive, it returns 0.
     * </p>
     *
     * @param page
     *            The current page number. Must be greater than 0.
     * @param size
     *            The number of items per page. Must be greater than 0.
     * @return The calculated offset. Returns 0 if page or size is null or less than
     *         or equal to 0.
     */
    public static int getOffset(Integer page, Integer size) {
        if (page == null || page <= 0 || size == null || size <= 0) return 0;
        return (page - 1) * size;
    }
}
