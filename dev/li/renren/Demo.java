package li.renren;

public class Demo {
    static Renren renren = new Renren();

    static String CODE = "YUTjopva4Aq2f8fubyheb1TGq64fkN8E";

    static String token = "{\"expires_in\":2593052,\"refresh_token\":\"186396|0.fIi2UKX5d5d7RS2GuBohAc7KVGNUcHWI.317908388.1356925347701\",\"user\":{\"id\":317908388,\"name\":\"黎明伟\",\"avatar\":[{\"type\":\"avatar\",\"url\":\"http://hdn.xnimg.cn/photos/hdn121/20110325/2355/h_head_gQLa_375d000035c72f76.jpg\"},{\"type\":\"tiny\",\"url\":\"http://hdn.xnimg.cn/photos/hdn121/20110325/2355/tiny_Uvpa_67378n019118.jpg\"},{\"type\":\"main\",\"url\":\"http://hdn.xnimg.cn/photos/hdn121/20110325/2355/h_main_Xbtz_375d000035c72f76.jpg\"},{\"type\":\"large\",\"url\":\"http://hdn.xnimg.cn/photos/hdn121/20110325/2355/h_large_2B7H_375d000035c72f76.jpg\"}]},\"access_token\":\"186396|6.564aa0eea78d26174ca4405143310285.2592000.1359518400-317908388\"}";

    public static void main(String[] args) {
        // renren.authorize();
        renren.token(CODE);
    }
}