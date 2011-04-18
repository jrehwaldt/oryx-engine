/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.hpi.oryxengine.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import de.hpi.oryxengine.exception.DalmatinaRuntimeException;

/**
 * Some IO-Utilities.
 */
public final class IoUtil {

    public static String readFileAsString(File file) {

        return getStringBufferFromFile(file);
    }

    public static String readFileAsString(String filePath) {

        return getStringBufferFromFile(getFile(filePath));
    }

    private static String getStringBufferFromFile(File file) {

        byte[] buffer = new byte[(int) file.length()];
        BufferedInputStream inputStream = null;

        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));

            inputStream.read(buffer);
        } catch (Exception e) {
            String errorMessage = "The file '" + file + "' could not be read. The following error occured: "
                + e.getMessage();
            throw new DalmatinaRuntimeException(errorMessage);
        } finally {
            IoUtil.closeSilently(inputStream);
        }

        return new String(buffer);
    }

    public static File getFile(String filePath) {

        URL url = IoUtil.class.getClassLoader().getResource(filePath);
        try {
            return new File(url.toURI());
        } catch (Exception e) {
            String errorMessage = "Couldn't get file '" + filePath + "'. The following error occurred: "
                + e.getMessage();
            throw new DalmatinaRuntimeException(errorMessage);
        }
    }

    /**
     * Closes the given stream. The same as calling {@link InputStream#close()}, but errors while closing are silently
     * ignored.
     */
    private static void closeSilently(InputStream inputStream) {

        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException ignore) {
            // Exception is silently ignored
        }
    }

    /**
     * Closes the given stream. The same as calling {@link OutputStream#close()}, but errors while closing are silently
     * ignored.
     */
    private static void closeSilently(OutputStream outputStream) {

        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException ignore) {
            // Exception is silently ignored
        }
    }

    /**
     * Hidden.
     */
    private IoUtil() {

    }
}
