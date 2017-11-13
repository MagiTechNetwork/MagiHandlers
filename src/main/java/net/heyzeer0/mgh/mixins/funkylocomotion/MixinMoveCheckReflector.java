package net.heyzeer0.mgh.mixins.funkylocomotion;

import com.rwtema.funkylocomotion.movepermissions.MoveCheckReflector;
import org.spongepowered.asm.mixin.*;

import java.util.HashMap;

/**
 * Created by Frani on 13/11/2017.
 */
@Pseudo
@Mixin(value = MoveCheckReflector.class, remap = false)
public abstract class MixinMoveCheckReflector {

    @Shadow @Final private static HashMap<Class<?>, Boolean> cache;

    @Shadow private static boolean _canMoveClass(Class<?> clazz) {return true; }

    @Overwrite
    public static boolean canMoveClass(Class<?> clazz) {
        if (clazz == null) return true;
        Boolean b = (Boolean)cache.get(clazz);
        if (b == null) {
            boolean bool = false;
            try {
                bool = _canMoveClass(clazz);
            } catch (Throwable e) {}
            b = Boolean.valueOf(bool);
            cache.put(clazz, b);
        }
        return b.booleanValue();
    }

}
