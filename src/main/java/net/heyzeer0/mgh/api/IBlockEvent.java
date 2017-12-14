package net.heyzeer0.mgh.api;

import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Frani on 18/10/2017.
 */
public interface IBlockEvent {

    IForgeTileEntity getTile();

    void setTile(TileEntity tile);

}
