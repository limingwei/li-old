package li.mvc.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import li.mvc.Context;
import li.util.StringUtil;

public class Uploader {
    private Map<String, String[]> parameters = new HashMap<String, String[]>();

    public List<FileMeta> upload(ServletRequest request, String uploadPath) throws IOException {
        List<FileMeta> fileMetas = new ArrayList<FileMeta>();
        String boundaryStart = "--" + getBoundary(request.getContentType());
        ServletInputStream inputStream = request.getInputStream();

        FileOutputStream fileOutputStream = null;// 文件输出流

        byte[] buf = new byte[1024];
        String name = null, fileName = null;// 文件名
        boolean isText = false;
        for (int len = 1; len > 0; len = inputStream.readLine(buf, 0, buf.length)) {// 循环读取ServletInputStream
            String temp = new String(buf, 0, len);
            if (boundaryStart.equalsIgnoreCase(temp.trim()) && null != fileOutputStream) {// 一个域的开始,结束写文件
                fileOutputStream.flush();
                fileOutputStream.close();
                fileOutputStream = null;// 是否为空会作为一个标志
            } else if (temp.startsWith("Content-Disposition") && temp.contains("filename=")) {// 文件头
                name = getName(temp);
                fileName = getFileName(temp);// 文件名
                isText = false;
                if (null != fileName && !fileName.isEmpty()) {// 是文件域的时候文件名非空
                    File file = file(uploadPath, name, fileName);// 文件名
                    fileOutputStream = new FileOutputStream(file);// 开始写一个新的文件
                    FileMeta fileMeta = new FileMeta(name, fileName, file);
                    fileMetas.add(fileMeta);
                }
            } else if (temp.startsWith("Content-Type")) {// Content-Type行
                inputStream.readLine(buf, 0, len); // 跳过Content-Type后的空行
            } else if (null != fileOutputStream) {// 如果在写一个文件的过程中
                fileOutputStream.write(buf, 0, len);// 写文件类容
            } else if (temp.startsWith("Content-Disposition") && !temp.contains("filename=")) {// 文本表单域
                name = getName(temp);// 获取文本域名
                isText = true;
            } else if (!temp.startsWith(boundaryStart) && null != name && isText && !temp.isEmpty()) {// 不是分隔符,非空,是文本域类容
                putParameter(name, temp);
            }
        }
        if (null != fileOutputStream) {// 结束
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        inputStream.close();
        Context.init(new LiHttpServletRequestWrapper(Context.getRequest(), parameters), Context.getResponse(), Context.getAction());
        return fileMetas;
    }

    /**
     * 设置文本域参数
     */
    private void putParameter(String name, String value) {
        if (null != value && !value.trim().isEmpty()) {
            String[] params = this.parameters.get(name);
            if (null == params) {
                this.parameters.put(name, new String[] { value.trim() });
            } else {
                String[] temp = new String[params.length + 1];
                for (int i = 0; i < params.length; i++) {
                    temp[i] = params[i];
                }
                temp[params.length] = value.trim();
                this.parameters.put(name, temp);
            }
        }
    }

    /**
     * 创建文件
     */
    private static File file(String uploadPath, String name, String fileName) {
        int i = fileName.indexOf("\\");
        if (i > 0) {// ie11里面filename不仅为文件名而为完整文件路径
            fileName = fileName.substring(i + 1);
        }
        System.out.println(fileName + "\t" + StringUtil.getEncoding(fileName));

        fileName = uploadPath + "//" + UUID.randomUUID() + "_" + name + "_" + fileName;

        System.out.println(uploadPath + "\t" + StringUtil.getEncoding(uploadPath));
        System.out.println(name + "\t" + StringUtil.getEncoding(name));
        System.out.println(fileName + "\t" + StringUtil.getEncoding(fileName));

        File file = new File(fileName);
        file.getParentFile().mkdirs();
        return file;
    }

    /**
     * 获取表单域名称
     */
    private static String getName(String temp) {
        int a = temp.indexOf("name=");
        int b = temp.indexOf(";", a);
        b = b < 0 ? temp.length() - 2 : b;
        if (a < 0) {
            return null;
        } else {
            return temp.substring(a + 6, b - 1);
        }
    }

    /**
     * 获取文件名
     */
    private static String getFileName(String temp) {
        int a = temp.indexOf("filename=");
        if (a < 0) {
            return null;
        } else {
            return temp.substring(a + 10, temp.lastIndexOf("\""));
        }
    }

    /**
     * 获取分隔符
     */
    private static String getBoundary(String contentType) {
        if (null == contentType) {
            return null;
        }
        int pos = contentType.indexOf(";");
        if (pos <= 10) {
            return null;
        }
        if (!contentType.substring(0, pos).equalsIgnoreCase("multipart/form-data")) {
            return null;
        }
        pos = contentType.indexOf("=", pos);
        if (pos < 0) {
            return null;
        }
        return contentType.substring(pos + 1);
    }

    /**
     * request的封装
     * 
     * @author : 明伟 
     */
    public class LiHttpServletRequestWrapper extends HttpServletRequestWrapper {
        private Map<String, String[]> parameters;

        public LiHttpServletRequestWrapper(ServletRequest request, Map<String, String[]> parameters) {
            super((HttpServletRequest) request);
            this.parameters = parameters;
        }

        public String getParameter(String name) {
            String[] parameterValues = this.getParameterValues(name);
            return null == parameterValues || parameterValues.length < 1 ? null : parameterValues[0];
        }

        public Map<String, String[]> getParameterMap() {
            return this.parameters;
        }

        public Enumeration<String> getParameterNames() {
            return new Vector(this.parameters.entrySet()).elements();
        }

        public String[] getParameterValues(String name) {
            return this.parameters.get(name);
        }
    }

}