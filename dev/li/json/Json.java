package li.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 将json理解为Map+List,以Token的方式读取,避免回溯等操作
 * 
 * @author wendal(wendal1985@gmail.com)
 */
public class Json {
    public static Object fromJson(String string) {
        return new JsonCompileImplV2().parse(new StringReader(string));
    }

    public static String toJson(Object obj) {
        Writer writer = new StringWriter();
        try {
            new JsonRenderImpl(writer, JsonFormat.nice()).render(obj);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }
}

/**
 * 将json理解为Map+List,以Token的方式读取,避免回溯等操作
 * 
 * @author wendal(wendal1985@gmail.com)
 */
class JsonCompileImplV2 {
    public Object parse(Reader reader) {
        return new JsonTokenScan(reader).read();
    }
}

final class JsonTokenScan {
    Reader reader;
    JsonToken token = new JsonToken();
    JsonToken nextToken = null;
    JsonToken nextToken2 = new JsonToken();

    static final Object END = new Object();
    static final Object COMMA = new Object();

    public JsonTokenScan(Reader reader) {
        this.reader = reader;
    }

    protected void _nextToken() {
        switch (token.type) {
        case MapStart:
        case MapEnd:
        case ListStart:
        case ListEnd:
        case MapPair:
        case Comma:
            return;
        case '\'':
            token.type = SimpleString;
            token.value = readString('\'');
            return;
        case '\"':
            token.type = SimpleString;
            token.value = readString('"');
            return;
        case ' ':
        case '\t':
        case '\n':
        case '\r':
            char c = 0;
            while (true) {
                c = nextChar();
                switch (c) {
                case ' ':
                case '\t':
                case '\n':
                case '\r':
                    continue;
                }
                break;
            }
            token.type = c;
            _nextToken();
            return;
        case '/':
            // 看来是注释哦
            skipComment();
            nextToken();
            return;
        default:
            StringBuilder sb = new StringBuilder();
            sb.append((char) token.type);
            // 看来只是尝试找到结束字符了
            OUT: while (true) {
                c = nextChar();
                switch (c) {
                case MapStart:
                case MapEnd:
                case ListStart:
                case ListEnd:
                case MapPair:
                case Comma:
                    nextToken = nextToken2;
                    nextToken.type = c;
                    break OUT;
                case ' ':
                case '\t':
                case '\r':
                case '\n':
                    break OUT;
                case '/':
                    skipComment();
                    break OUT;
                }
                sb.append(c);
            }
            token.type = OtherString;
            token.value = sb.toString();
            return;
        }
    }

    protected void nextToken() {
        if (nextToken != null) {
            token.type = nextToken.type;
            token.value = nextToken.value;
            nextToken = null;
            return;
        }
        token.type = nextChar();
        _nextToken();
    }

    protected void skipComment() {
        char c = nextChar();
        switch (c) {
        case '/': // 单行注释
            while (nextChar() != '\n') {}
            return;
        case '*':
            char c2 = c;
            while (true) {
                while ((c = nextChar()) != '/') {
                    c2 = c;
                }
                if (c2 == '*')
                    return;
            }
        default:
            throw unexpectChar(c);
        }
    }

    protected String readString(char endEnd) {
        StringBuilder sb = new StringBuilder();
        char c = 0;
        while ((c = nextChar()) != endEnd) {
            switch (c) {
            case '\\':
                c = parseSp();
                break;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    protected Map<String, Object> readMap() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        boolean hasComma = false;
        OUT: while (true) {
            nextToken();
            switch (token.type) {
            case MapEnd:
                break OUT;
            case SimpleString:
            case OtherString:
                String key = token.value;
                nextToken();
                if (token.type != MapPair) {
                    throw unexpectChar((char) token.type);
                }
                Object obj = readObject(MapEnd);
                if (obj == COMMA) {
                    if (hasComma)
                        throw unexpectChar((char) Comma);
                    hasComma = true;
                    continue;
                }
                if (obj == END)
                    throw unexpectChar((char) token.type);
                map.put(key, obj);
                hasComma = false;
                break;
            case Comma:
                continue;
            default:
                throw unexpectChar((char) token.type);
            }
        }
        return map;
    }

    protected List<Object> readList() {
        List<Object> list = new ArrayList<Object>();
        boolean hasComma = false;
        while (true) {
            Object obj = readObject(ListEnd);
            if (obj == END)
                break;
            if (obj == COMMA) {
                if (hasComma)
                    throw unexpectChar((char) Comma);
                hasComma = true;
                continue;
            }
            list.add(obj);
            hasComma = false;
        }
        return list;
    }

    protected Object readObject(int endTag) {
        nextToken();
        switch (token.type) {
        case MapStart:
            return readMap();
        case ListStart:
            return readList();
        case SimpleString:
            return token.value;
        case OtherString:
            String value = token.value;
            int len = value.length();
            if (len == 0)
                return "";
            switch (value.charAt(0)) {
            case 't':
                if ("true".equals(value))
                    return true;
                break;
            case 'f':
                if ("false".equals(value))
                    return false;
                break;
            case 'n':
                if ("null".endsWith(value))
                    return null;
                break;
            case 'u':
                if ("undefined".endsWith(value))
                    return null;
                break;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '.':
            case '-':
                // 看来是数字哦
                if (token.value.length() > 0) {
                    switch (token.value.charAt(token.value.length() - 1)) {
                    case 'l':
                    case 'L':
                        return Long.parseLong(token.value.substring(0, token.value.length() - 1));
                    case 'f':
                    case 'F':
                        return Float.parseFloat(token.value.substring(0, token.value.length() - 1));
                    default:
                        if (token.value.contains("e") || token.value.contains("E")) {
                            return new BigDecimal(token.value);
                        }
                        if (token.value.contains(".")) {
                            return Double.parseDouble(token.value);
                        }
                    }
                }
                long n = Long.parseLong(token.value);
                if (Integer.MAX_VALUE >= n && n >= Integer.MIN_VALUE) {
                    return (int) n;
                }
                return n;
            }
            throw new RuntimeException(row + col + value.charAt(0) + "Unexpect String = " + value);
        default:
            if (token.type == endTag)
                return END;
            if (token.type == Comma)
                return COMMA;
            throw unexpectChar((char) token.type);
        }
    }

    public Object read() {
        int c = 0;
        boolean add = false;
        OUT: while (true) {
            c = readChar();
            switch (c) {
            case -1:
                return null;
            case ' ':
            case '\t':
            case '\n':
            case '\r':
                continue;
            case '/':
                skipComment();
                break;
            default:
                add = true;
                break OUT;
            }
        }

        switch (c) {
        case 'v':
            while (nextChar() != MapStart) {}
            return readMap();
        case MapStart:
            return readMap();
        case ListStart:
            return readList();
        case '\'':
        case '"':
            return readString((char) c);
        default:
            nextToken = nextToken2;
            nextToken.type = OtherString;
            if (add)
                nextToken.value = (char) c + readAll(reader);
            else
                nextToken.value = readAll(reader);
            return readObject(-1);
        }
    }

    char nextChar() {
        int c = readChar();
        if (c == -1)
            throw new RuntimeException("Unexpect EOF");
        return (char) c;
    }

    protected char parseSp() {
        char c = nextChar();
        switch (c) {
        case 'n':
            return '\n';
        case 'r':
            return '\r';
        case 't':
            return '\t';
        case '\\':
            return '\\';
        case '\'':
            return '\'';
        case '\"':
            return '"';
        case '/':
            return '/';
        case 'u':
            char[] hex = new char[4];
            for (int i = 0; i < 4; i++)
                hex[i] = nextChar();
            return (char) Integer.valueOf(new String(hex), 16).intValue();
        case 'b': // 这个支持一下又何妨?
            return ' ';// 空格
        case 'f':
            return '\f';
        default:
            throw unexpectChar(c);
        }
    }

    int row = 1;
    int col = 0;

    private int readChar() {
        try {
            int c = reader.read();
            switch (c) {
            case -1:
                break;
            case '\n':
                row++;
                col = 0;
            default:
                col++;
                break;
            }
            return c;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static final int MapStart = '{';
    static final int MapEnd = '}';
    static final int ListStart = '[';
    static final int ListEnd = ']';
    static final int MapPair = ':';
    static final int SimpleString = 0;
    static final int OtherString = 1;
    static final int Comma = ',';

    protected RuntimeException unexpectChar(char c) {
        return new RuntimeException(row + col + c + "Unexpect Char");
    }

    /**
     * 从一个文本输入流读取所有内容，并将该流关闭
     * 
     * @param reader 文本输入流
     * @return 输入流所有内容
     */
    public static String readAll(Reader reader) {
        if (!(reader instanceof BufferedReader))
            reader = new BufferedReader(reader);
        try {
            StringBuilder sb = new StringBuilder();

            char[] data = new char[64];
            int len;
            while (true) {
                if ((len = reader.read(data)) == -1)
                    break;
                sb.append(data, 0, len);
            }
            return sb.toString();
        } catch (IOException e) {
            new RuntimeException(e);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                new RuntimeException(e);
            }
        }
        return null;
    }
}

class JsonToken {
    int type;
    String value;

    public String toString() {
        return "[" + (char) type + " " + value + "]" + hashCode();
    }
}

class JsonRenderImpl {
    private static String NL = "\n";

    private JsonFormat format;

    private Writer writer;

    private Set<Object> memo = new HashSet<Object>();

    public void render(Object obj) throws IOException {
        if (null == obj) {
            writer.write("null");
        } else if (obj instanceof JsonRenderImpl) {
            ((JsonRenderImpl) obj).render(null);
        } else if (obj instanceof Class) {
            string2Json(((Class<?>) obj).getName());
        } else {
            // 枚举
            if (obj.getClass().isEnum()) {
                string2Json(((Enum) obj).name());
            }
            // 数字，布尔等
            else if (isNumber(obj.getClass()) || isBoolean(obj.getClass())) {
                writer.append(obj.toString());
            }
            // 字符串
            else if (CharSequence.class.isAssignableFrom(obj.getClass()) || (is(obj.getClass(), char.class) || is(obj.getClass(), Character.class))) {
                string2Json(obj.toString());
            }
            // 日期时间
            else if (isDateTimeLike(obj.getClass())) {
                string2Json(obj.toString());
            }
            // 其他
            else {
                // Map
                if (obj instanceof Map) {
                    map2Json((Map) obj);
                }
                // 集合
                else if (obj instanceof Collection) {
                    coll2Json((Collection) obj);
                }
                // 数组
                else if (obj.getClass().isArray()) {
                    array2Json(obj);
                }
                // 普通 Java 对象
                else {
                    memo.add(obj);
                    pojo2Json(obj);
                    memo.remove(obj);
                }
            }
        }
    }

    private boolean is(Class<? extends Object> class1, Class<Character> class2) {
        // TODO Auto-generated method stub
        return false;
    }

    private boolean isBoolean(Class<? extends Object> class1) {
        // TODO Auto-generated method stub
        return false;
    }

    private boolean isNumber(Class<? extends Object> class1) {
        // TODO Auto-generated method stub
        return false;
    }

    private boolean isDateTimeLike(Class<? extends Object> class1) {
        // TODO Auto-generated method stub
        return false;
    }

    public JsonRenderImpl(Writer writer, JsonFormat format) {
        this.format = format;
        this.writer = writer;
    }

    private static boolean isCompact(JsonRenderImpl render) {
        return render.format.isCompact();
    }

    private static final Pattern p = Pattern.compile("^[a-z_A-Z$]+[a-zA-Z_0-9$]*$");

    private void appendName(String name) throws IOException {
        if (format.isQuoteName() || !p.matcher(name).find())
            string2Json(name);
        else
            writer.append(name);
    }

    private void appendPairBegin() throws IOException {
        if (!isCompact(this))
            writer.append(NL).append(dup(format.getIndentBy(), format.getIndent()));
    }

    private CharSequence dup(String indentBy, int indent) {
        // TODO Auto-generated method stub
        return null;
    }

    private void appendPairSep() throws IOException {
        writer.append(!isCompact(this) ? " :" : ":");
    }

    protected void appendPair(boolean needPairEnd, String name, Object value) throws IOException {
        appendPairBegin();
        appendName(name);
        appendPairSep();
        render(value);
        if (needPairEnd) {
            appendPairEnd();
        }
    }

    private boolean isIgnore(String name, Object value) {
        if (null == value && format.isIgnoreNull())
            return true;
        return format.ignore(name);
    }

    private void appendPairEnd() throws IOException {
        writer.append(',');
    }

    private void appendBraceBegin() throws IOException {
        writer.append('{');
    }

    private void appendBraceEnd() throws IOException {
        if (!isCompact(this))
            writer.append(NL).append(dup(format.getIndentBy(), format.getIndent()));
        writer.append('}');
    }

    static class Pair {
        public Pair(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        String name;
        Object value;
    }

    private void map2Json(Map map) throws IOException {
        if (null == map)
            return;
        appendBraceBegin();
        increaseFormatIndent();
        ArrayList<Pair> list = new ArrayList<Pair>(map.size());
        Set<Entry<?, ?>> entrySet = map.entrySet();
        for (Entry entry : entrySet) {
            String name = null == entry.getKey() ? "null" : entry.getKey().toString();
            Object value = entry.getValue();
            if (!this.isIgnore(name, value))
                list.add(new Pair(name, value));
        }
        writeItem(list);
    }

    private void pojo2Json(Object obj) throws IOException {
        if (null == obj)
            return;
        Class<?> type = obj.getClass();

        List<Field> fields = getFields(type);

        appendBraceBegin();
        increaseFormatIndent();
        ArrayList<Pair> list = new ArrayList<Pair>(fields.size());
        for (Field jef : fields) {
            String name = jef.getName();
            try {
                Object value = get(obj, jef);
                // 判断是否应该被忽略
                if (!this.isIgnore(name, value)) {
                    // 以前曾经输出过 ...
                    if (null != value) {
                        // zozoh: 循环引用的默认行为，应该为 null，以便和其他语言交换数据
                        if (memo.contains(value))
                            value = null;
                    }
                    // 加入输出列表 ...
                    list.add(new Pair(name, value));
                }
            } catch (Exception e) {}
        }
        writeItem(list);
    }

    private Object get(Object obj, Field jef) {
        // TODO Auto-generated method stub
        return null;
    }

    private List<Field> getFields(Class<?> type) {
        // TODO Auto-generated method stub
        return null;
    }

    private void writeItem(List<Pair> list) throws IOException {
        Iterator<Pair> it = list.iterator();
        while (it.hasNext()) {
            Pair p = it.next();
            appendPair(it.hasNext(), p.name, p.value);
        }
        decreaseFormatIndent();
        appendBraceEnd();
    }

    private void decreaseFormatIndent() {
        if (!isCompact(this))
            format.decreaseIndent();
    }

    private void increaseFormatIndent() {
        if (!isCompact(this))
            format.increaseIndent();
    }

    private void string2Json(String s) throws IOException {
        if (null == s)
            writer.append("null");
        else {
            char[] cs = s.toCharArray();
            writer.append(format.getSeparator());
            for (char c : cs) {
                switch (c) {
                case '"':
                    writer.append("\\\"");
                    break;
                case '\n':
                    writer.append("\\n");
                    break;
                case '\t':
                    writer.append("\\t");
                    break;
                case '\r':
                    writer.append("\\r");
                    break;
                case '\\':
                    writer.append("\\\\");
                    break;
                default:
                    if (c >= 256 && format.isAutoUnicode())
                        writer.append("\\u").append(Integer.toHexString(c).toUpperCase());
                    else
                        writer.append(c);
                }
            }
            writer.append(format.getSeparator());
        }
    }

    private void array2Json(Object obj) throws IOException {
        writer.append('[');
        int len = Array.getLength(obj) - 1;
        if (len > -1) {
            int i;
            for (i = 0; i < len; i++) {
                render(Array.get(obj, i));
                appendPairEnd();
                writer.append(' ');
            }
            render(Array.get(obj, i));
        }
        writer.append(']');
    }

    private void coll2Json(Collection iterable) throws IOException {
        writer.append('[');
        for (Iterator<?> it = iterable.iterator(); it.hasNext();) {
            render(it.next());
            if (it.hasNext()) {
                appendPairEnd();
                writer.append(' ');
            } else
                break;
        }
        writer.append(']');
    }
}

/**
 * 描述Json输出的格式
 * 
 * @author zozoh(zozohtnt@gmail.com)
 * @author Wendal(wendal1985@gmail.com)
 */
class JsonFormat {
    /**
     * 紧凑模式 -- 无换行,忽略null值
     */
    public static JsonFormat compact() {
        return new JsonFormat(true).setIgnoreNull(true);
    }

    /**
     * 全部输出模式 -- 换行,不忽略null值
     */
    public static JsonFormat full() {
        return new JsonFormat(false).setIgnoreNull(false);
    }

    /**
     * 一般模式 -- 换行,但忽略null值
     */
    public static JsonFormat nice() {
        return new JsonFormat(false).setIgnoreNull(true);
    }

    /**
     * 为了打印出来容易看，把名字去掉引号
     */
    public static JsonFormat forLook() {
        return new JsonFormat(false).setQuoteName(false).setIgnoreNull(true);
    }

    public JsonFormat() {
        this(true);
    }

    public JsonFormat(boolean compact) {
        this.compact = compact;
        this.indentBy = "   ";
        this.quoteName = true;
        this.separator = '\"';
    }

    /**
     * 缩进
     */
    private int indent;
    /**
     * 缩进时用的字符串
     */
    private String indentBy;
    /**
     * 紧凑
     */
    private boolean compact;
    private boolean quoteName;
    /**
     * 是否忽略null值
     */
    private boolean ignoreNull;
    private Pattern actived;
    private Pattern locked;

    /**
     * 分隔符
     */
    private char separator;
    /**
     * 是否自动将值应用Unicode编码
     */
    private boolean autoUnicode;

    public boolean ignore(String name) {
        if (null != actived)
            return !actived.matcher(name).find();
        if (null != locked)
            return locked.matcher(name).find();
        return false;
    }

    public boolean isCompact() {
        return compact;
    }

    public JsonFormat setCompact(boolean compact) {
        this.compact = compact;
        return this;
    }

    public int getIndent() {
        return indent;
    }

    public JsonFormat setIndent(int indent) {
        this.indent = indent;
        return this;
    }

    public JsonFormat increaseIndent() {
        this.indent++;
        return this;
    }

    public JsonFormat decreaseIndent() {
        this.indent--;
        return this;
    }

    public String getIndentBy() {
        return indentBy;
    }

    public JsonFormat setIndentBy(String indentBy) {
        this.indentBy = indentBy;
        return this;
    }

    public boolean isQuoteName() {
        return quoteName;
    }

    public JsonFormat setQuoteName(boolean qn) {
        this.quoteName = qn;
        return this;
    }

    public boolean isIgnoreNull() {
        return ignoreNull;
    }

    public JsonFormat setIgnoreNull(boolean ignoreNull) {
        this.ignoreNull = ignoreNull;
        return this;
    }

    public JsonFormat setActived(String regex) {
        this.actived = Pattern.compile(regex);
        return this;
    }

    public JsonFormat setLocked(String regex) {
        this.locked = Pattern.compile(regex);
        return this;
    }

    public JsonFormat setSeparator(char separator) {
        this.separator = separator;
        return this;
    }

    public char getSeparator() {
        return separator;
    }

    public JsonFormat setAutoUnicode(boolean autoUnicode) {
        this.autoUnicode = autoUnicode;
        return this;
    }

    public boolean isAutoUnicode() {
        return autoUnicode;
    }
}