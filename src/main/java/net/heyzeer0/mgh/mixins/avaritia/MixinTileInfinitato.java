package net.heyzeer0.mgh.mixins.avaritia;

import com.google.common.collect.Lists;
import fox.spiteful.avaritia.compat.botania.TileInfinitato;
import net.heyzeer0.mgh.api.forge.ForgeStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Created by Frani on 17/03/2018.
 */
@Mixin(value = TileInfinitato.class, remap = false)
public abstract class MixinTileInfinitato {

    private Instant lastTouched = Instant.now();

    @Inject(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;func_72872_a(Ljava/lang/Class;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;"), cancellable = true)
    private void onInteract(CallbackInfo ci) {
        if (ChronoUnit.SECONDS.between(lastTouched, Instant.now()) <= 120) {
            ForgeStack.getStack().getCurrentEntityPlayer().ifPresent(p -> {
                if (!(p instanceof FakePlayer)) {
                    p.addChatMessage(new ChatComponentText("§7Espere alguns segundos antes de usar novamente!"));
                    List<PotionEffect> effects = Lists.newArrayList(p.getActivePotionEffects());
                    p.clearActivePotions();
                    effects.forEach(p::addPotionEffect);
                }
            });
            ci.cancel();
        } else {
            lastTouched = Instant.now();
        }
    }

}
