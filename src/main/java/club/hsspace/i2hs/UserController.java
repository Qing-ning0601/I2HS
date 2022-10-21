package club.hsspace.i2hs;

import club.hsspace.i2hs.entity.User;
import club.hsspace.sqlplugin.SQLParam;
import club.hsspace.sqlplugin.SQLReturn;
import club.hsspace.sqlplugin.SQLStatement;
import club.hsspace.whypps.framework.app.InterceptorHandle;
import club.hsspace.whypps.framework.app.annotation.ApiDataMsg;
import club.hsspace.whypps.framework.app.annotation.AppInterface;
import club.hsspace.whypps.framework.app.annotation.DataParam;
import club.hsspace.whypps.framework.app.annotation.Interceptor;
import club.hsspace.whypps.model.senior.Code;
import club.hsspace.whypps.util.MD5Tools;
import club.hsspace.whypps.util.NumberTools;

@AppInterface
public class UserController {

    @Interceptor(sort = 1000, list = "/login")
    public void loginInterceptor(UserMount userMount, InterceptorHandle interceptorHandle) {
        boolean b = userMount.hasUser();
        if (!b) {
            interceptorHandle.interruptReturn(Code.of(21041, "密码错误"));
        }
    }

    @ApiDataMsg("/login")
    @SQLStatement(namespace = "mapper.User.selectByUserName")
    public Code login(UserMount userMount, @SQLParam("userName") @DataParam("userName") String userName,
                      @DataParam("password") String password, @SQLReturn User user) {

        byte[] psw = MD5Tools.SHA256(password.getBytes());
        psw = MD5Tools.SHA256(NumberTools.bytesMerger(psw, NumberTools.hexString2Bytes(user.getSalt())));

        if (NumberTools.bytes2HexString(psw).equals(user.getPassword())) {
            userMount.setUser(user);
            return Code.OK;
        } else {
            return Code.of(21041, "密码错误");
        }
    }

}