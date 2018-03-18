package net.heyzeer0.mgh.mixins.worldedit;

import com.google.common.collect.Sets;
import com.sk89q.worldedit.forge.ForgeWorld;
import org.bukkit.craftbukkit.v1_7_R4.util.LongHashSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * Created by Frani on 17/03/2018.
 */
@Mixin(value = ForgeWorld.class, remap = false)
public abstract class MixinForgeWorld {

    private LongHashSet longHashSet;

    @Redirect(method = "regenerate", at = @At(value = "INVOKE", target = "Ljava/lang/reflect/Field;get(Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0))
    private Object redirectGet(Field instance, Object otherObj) {
        try {
            longHashSet = (LongHashSet) instance.get(otherObj);
            return Sets.newHashSet(longHashSet.toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Redirect(method = "regenerate", at = @At(value = "INVOKE", target = "Ljava/util/Set;remove(Ljava/lang/Object;)Z"))
    private boolean redirectRemove(Set set, Object obj) {
        return longHashSet.remove((Long) obj);
    }

}
