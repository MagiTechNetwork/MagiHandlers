package net.heyzeer0.mgh.hacks.tt;

import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import thaumcraft.common.lib.FakeThaumcraftPlayer;
import thaumic.tinkerer.common.block.tile.tablet.TileAnimationTablet;

/**
 * Created by Frani on 23/10/2017.
 */
public class FakeTabletFakePlayer extends FakeThaumcraftPlayer {

    TileAnimationTablet tablet;

    public FakeTabletFakePlayer(TileAnimationTablet tablet) {
        super(tablet.getWorldObj(), ((IForgeTileEntity) tablet).getFakePlayer().getGameProfile());
        this.tablet = tablet;
    }

    @Override
    public void setDead() {
        inventory.clearInventory(null, -1);
        super.setDead();
    }

    @Override
    public void openGui(Object mod, int modGuiId, World world, int x, int y, int z) {
        // NO-OP
    }

    @Override
    public void onUpdate() {
        capabilities.isCreativeMode = false;

        posX = tablet.xCoord + 0.5;
        posY = tablet.yCoord + 1.6;
        posZ = tablet.zCoord + 0.5;

        if (riddenByEntity != null)
            riddenByEntity.ridingEntity = null;
        if (ridingEntity != null)
            ridingEntity.riddenByEntity = null;
        riddenByEntity = null;
        ridingEntity = null;

        motionX = motionY = motionZ = 0;
        setHealth(20);
        isDead = false;

        int meta = tablet.getBlockMetadata() & 7;
        int rotation = meta == 2 ? 180 : meta == 3 ? 0 : meta == 4 ? 90 : -90;
        rotationYaw = rotationYawHead = rotation;
        rotationPitch = -15;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (i != inventory.currentItem) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (stack != null) {
                    entityDropItem(stack, 1.0f);
                    inventory.setInventorySlotContents(i, null);
                }
            }
        }
    }

    @Override
    public void addChatMessage(IChatComponent var1) {

    }

    @Override
    public ChunkCoordinates getPlayerCoordinates() {
        return new ChunkCoordinates(tablet.xCoord, tablet.yCoord, tablet.zCoord);
    }

}
