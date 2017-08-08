package net.heyzeer0.mgh.mixins.ic2;

import com.mojang.authlib.GameProfile;
import ic2.api.item.ITerraformingBP;
import ic2.core.block.machine.tileentity.TileEntityElectricMachine;
import net.heyzeer0.mgh.hacks.ITileEntityOwnable;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

/**
 * Created by Frani on 06/08/2017.
 */

@Pseudo
@Mixin(targets = "ic2/core/block/machine/tileentity/TileEntityTerra", remap = false)
public abstract class MixinTileEntityTerra extends TileEntityElectricMachine implements ITileEntityOwnable {

    public MixinTileEntityTerra(int maxenergy, int tier1, int oldDischargeIndex) {
        super(maxenergy, tier1, oldDischargeIndex);
    }

    private FakePlayer fakePlayer;

    private FakePlayer getFakePlayer() {
        if (fakePlayer == null) {
            if (this.getOwner() != null && this.getUUID() != null) {
                fakePlayer = FakePlayerFactory.get((WorldServer) this.worldObj, new GameProfile(UUID.fromString(getUUID()), getOwner()));
            } else {
                fakePlayer = FakePlayerFactory.getMinecraft((WorldServer)this.worldObj);
            }
        }
        return fakePlayer;
    }

    @Redirect(method = "updateEntityServer", at = @At(value = "INVOKE", target = "Lic2/api/item/ITerraformingBP;terraform(Lnet/minecraft/world/World;III)Z"))
    public boolean redirectTerraform(ITerraformingBP instance, World world, int x, int z, int y) {
        EntityPlayer player = getOwner() == null ? null : MinecraftServer.getServer().getConfigurationManager().func_152612_a(getOwner());
        BlockEvent.BreakEvent e = MixinManager.generateBlockEvent(x, y, z, world, player == null ? fakePlayer : player);
        MinecraftForge.EVENT_BUS.post(e);
        if(!e.isCanceled()) {
            return instance.terraform(world, x, z, y);
        } else {
            return false;
        }
    }

}
