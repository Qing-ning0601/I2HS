package club.hsspace.i2hs;

import club.hsspace.i2hs.entity.User;
import club.hsspace.whypps.framework.app.annotation.MountEntity;

@MountEntity
public interface UserMount {

    User getUser();

    void setUser(User user);

    boolean hasUser(); //可选

}
