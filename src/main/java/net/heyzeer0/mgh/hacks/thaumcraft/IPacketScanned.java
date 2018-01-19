package net.heyzeer0.mgh.hacks.thaumcraft;

/**
 * Created by Frani on 19/01/2018.
 */
public interface IPacketScanned {

    int getDim();

    byte getType();

    int getId();

    int getMd();

    int getEntityId();

    String getPhenomena();

    String getPrefix();

    String getPlayerName();

    String getEntityUuid();

}
