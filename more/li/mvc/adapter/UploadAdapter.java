package li.mvc.adapter;

import java.util.List;

import javax.servlet.ServletRequest;

import li.dao.Page;
import li.model.Action;
import li.mvc.Context;
import li.util.Convert;
import li.util.Verify;

/**
 * 文件上传适配器
 * 
 * @author 明伟
 */
public class UploadAdapter extends AbstractAdapter {
    private Uploader uploader = new Uploader();

    private List<FileMeta> fileMetas;

    /***
     * 要解析fileMeta和基本类型参数,TODO 要不要做数据对象参数
     */
    public Boolean match(Class<?> type) {
        if (FileMeta.class.isAssignableFrom(type)) {
            return true;
        } else if (type.isArray() && FileMeta.class.isAssignableFrom(type.getComponentType())) {
            return true;
        } else if (Verify.basicType(type) && !type.isArray()) {
            return true;
        } else if (Verify.basicType(type) && type.isArray()) {
            return true;
        } else if (Page.class.isAssignableFrom(type)) {
            return true;
        }
        return false;
    }

    /**
     * adapt
     */
    public Object adapt(ServletRequest request, Integer argIndex) throws Exception {
        Action action = Context.getAction();
        Class<?> type = action.argTypes[argIndex];
        String key = (null == action.argAnnotations[argIndex]) ? action.argNames[argIndex] : action.argAnnotations[argIndex].value();// ParameterKey
        if (FileMeta.class.isAssignableFrom(type)) {
            if (null == fileMetas) {
                fileMetas = uploader.upload(request, Context.getRootPath() + "\\temp\\");
            }
            return fileMetas.isEmpty() ? null : fileMetas.get(0);
        } else if (type.isArray() && FileMeta.class.isAssignableFrom(type.getComponentType())) {
            if (null == fileMetas) {
                fileMetas = uploader.upload(request, Context.getRootPath() + "\\temp\\");
            }
            return fileMetas.toArray(new FileMeta[] {});
        } else if (Verify.basicType(type) && !type.isArray()) {
            return Convert.toType(type, Context.getRequest().getParameter(key));
        } else if (Verify.basicType(type) && type.isArray()) {
            return Context.getArray(type.getComponentType(), key);
        } else if (Page.class.isAssignableFrom(type)) {
            return Context.getPage(key);
        }
        return null;
    }
}