package net.heyzeer0.mgh.hacks;

import net.minecraftforge.common.util.FakePlayer;

/**
 * Created by Frani on 06/08/2017.
 */
public interface ITileEntityOwnable {

    String getOwner();
    String getUUID();
    void setOwner(String owner);
    void setUUID(String uuid);
    FakePlayer getFakePlayer();

}
