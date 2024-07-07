package me.teawin.teapilot.proposal;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;

public class ParticleManager {
    private static final BiMap<ParticleType<?>, String> PARTICLE_TYPE_MAP = HashBiMap.create();

    private static void register(ParticleType<?> type, String name) {
        PARTICLE_TYPE_MAP.put(type, name);
    }

    static {
        register(ParticleTypes.AMBIENT_ENTITY_EFFECT, "AMBIENT_ENTITY_EFFECT");
        register(ParticleTypes.ANGRY_VILLAGER, "ANGRY_VILLAGER");
        register(ParticleTypes.BLOCK, "BLOCK");
        register(ParticleTypes.BLOCK_MARKER, "BLOCK_MARKER");
        register(ParticleTypes.BUBBLE, "BUBBLE");
        register(ParticleTypes.CLOUD, "CLOUD");
        register(ParticleTypes.CRIT, "CRIT");
        register(ParticleTypes.DAMAGE_INDICATOR, "DAMAGE_INDICATOR");
        register(ParticleTypes.DRAGON_BREATH, "DRAGON_BREATH");
        register(ParticleTypes.DRIPPING_LAVA, "DRIPPING_LAVA");
        register(ParticleTypes.FALLING_LAVA, "FALLING_LAVA");
        register(ParticleTypes.LANDING_LAVA, "LANDING_LAVA");
        register(ParticleTypes.DRIPPING_WATER, "DRIPPING_WATER");
        register(ParticleTypes.FALLING_WATER, "FALLING_WATER");
        register(ParticleTypes.DUST, "DUST");
        register(ParticleTypes.DUST_COLOR_TRANSITION, "DUST_COLOR_TRANSITION");
        register(ParticleTypes.EFFECT, "EFFECT");
        register(ParticleTypes.ELDER_GUARDIAN, "ELDER_GUARDIAN");
        register(ParticleTypes.ENCHANTED_HIT, "ENCHANTED_HIT");
        register(ParticleTypes.ENCHANT, "ENCHANT");
        register(ParticleTypes.END_ROD, "END_ROD");
        register(ParticleTypes.ENTITY_EFFECT, "ENTITY_EFFECT");
        register(ParticleTypes.EXPLOSION_EMITTER, "EXPLOSION_EMITTER");
        register(ParticleTypes.EXPLOSION, "EXPLOSION");
        register(ParticleTypes.SONIC_BOOM, "SONIC_BOOM");
        register(ParticleTypes.FALLING_DUST, "FALLING_DUST");
        register(ParticleTypes.FIREWORK, "FIREWORK");
        register(ParticleTypes.FISHING, "FISHING");
        register(ParticleTypes.FLAME, "FLAME");
        register(ParticleTypes.CHERRY_LEAVES, "CHERRY_LEAVES");
        register(ParticleTypes.SCULK_SOUL, "SCULK_SOUL");
        register(ParticleTypes.SCULK_CHARGE, "SCULK_CHARGE");
        register(ParticleTypes.SCULK_CHARGE_POP, "SCULK_CHARGE_POP");
        register(ParticleTypes.SOUL_FIRE_FLAME, "SOUL_FIRE_FLAME");
        register(ParticleTypes.SOUL, "SOUL");
        register(ParticleTypes.FLASH, "FLASH");
        register(ParticleTypes.HAPPY_VILLAGER, "HAPPY_VILLAGER");
        register(ParticleTypes.COMPOSTER, "COMPOSTER");
        register(ParticleTypes.HEART, "HEART");
        register(ParticleTypes.INSTANT_EFFECT, "INSTANT_EFFECT");
        register(ParticleTypes.ITEM, "ITEM");
        register(ParticleTypes.VIBRATION, "VIBRATION");
        register(ParticleTypes.ITEM_SLIME, "ITEM_SLIME");
        register(ParticleTypes.ITEM_SNOWBALL, "ITEM_SNOWBALL");
        register(ParticleTypes.LARGE_SMOKE, "LARGE_SMOKE");
        register(ParticleTypes.LAVA, "LAVA");
        register(ParticleTypes.MYCELIUM, "MYCELIUM");
        register(ParticleTypes.NOTE, "NOTE");
        register(ParticleTypes.POOF, "POOF");
        register(ParticleTypes.PORTAL, "PORTAL");
        register(ParticleTypes.RAIN, "RAIN");
        register(ParticleTypes.SMOKE, "SMOKE");
        register(ParticleTypes.SNEEZE, "SNEEZE");
        register(ParticleTypes.SPIT, "SPIT");
        register(ParticleTypes.SQUID_INK, "SQUID_INK");
        register(ParticleTypes.SWEEP_ATTACK, "SWEEP_ATTACK");
        register(ParticleTypes.TOTEM_OF_UNDYING, "TOTEM_OF_UNDYING");
        register(ParticleTypes.UNDERWATER, "UNDERWATER");
        register(ParticleTypes.SPLASH, "SPLASH");
        register(ParticleTypes.WITCH, "WITCH");
        register(ParticleTypes.BUBBLE_POP, "BUBBLE_POP");
        register(ParticleTypes.CURRENT_DOWN, "CURRENT_DOWN");
        register(ParticleTypes.BUBBLE_COLUMN_UP, "BUBBLE_COLUMN_UP");
        register(ParticleTypes.NAUTILUS, "NAUTILUS");
        register(ParticleTypes.DOLPHIN, "DOLPHIN");
        register(ParticleTypes.CAMPFIRE_COSY_SMOKE, "CAMPFIRE_COSY_SMOKE");
        register(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, "CAMPFIRE_SIGNAL_SMOKE");
        register(ParticleTypes.DRIPPING_HONEY, "DRIPPING_HONEY");
        register(ParticleTypes.FALLING_HONEY, "FALLING_HONEY");
        register(ParticleTypes.LANDING_HONEY, "LANDING_HONEY");
        register(ParticleTypes.FALLING_NECTAR, "FALLING_NECTAR");
        register(ParticleTypes.FALLING_SPORE_BLOSSOM, "FALLING_SPORE_BLOSSOM");
        register(ParticleTypes.ASH, "ASH");
        register(ParticleTypes.CRIMSON_SPORE, "CRIMSON_SPORE");
        register(ParticleTypes.WARPED_SPORE, "WARPED_SPORE");
        register(ParticleTypes.SPORE_BLOSSOM_AIR, "SPORE_BLOSSOM_AIR");
        register(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, "DRIPPING_OBSIDIAN_TEAR");
        register(ParticleTypes.FALLING_OBSIDIAN_TEAR, "FALLING_OBSIDIAN_TEAR");
        register(ParticleTypes.LANDING_OBSIDIAN_TEAR, "LANDING_OBSIDIAN_TEAR");
        register(ParticleTypes.REVERSE_PORTAL, "REVERSE_PORTAL");
        register(ParticleTypes.WHITE_ASH, "WHITE_ASH");
        register(ParticleTypes.SMALL_FLAME, "SMALL_FLAME");
        register(ParticleTypes.SNOWFLAKE, "SNOWFLAKE");
        register(ParticleTypes.DRIPPING_DRIPSTONE_LAVA, "DRIPPING_DRIPSTONE_LAVA");
        register(ParticleTypes.FALLING_DRIPSTONE_LAVA, "FALLING_DRIPSTONE_LAVA");
        register(ParticleTypes.DRIPPING_DRIPSTONE_WATER, "DRIPPING_DRIPSTONE_WATER");
        register(ParticleTypes.FALLING_DRIPSTONE_WATER, "FALLING_DRIPSTONE_WATER");
        register(ParticleTypes.GLOW_SQUID_INK, "GLOW_SQUID_INK");
        register(ParticleTypes.GLOW, "GLOW");
        register(ParticleTypes.WAX_ON, "WAX_ON");
        register(ParticleTypes.WAX_OFF, "WAX_OFF");
        register(ParticleTypes.ELECTRIC_SPARK, "ELECTRIC_SPARK");
        register(ParticleTypes.SCRAPE, "SCRAPE");
        register(ParticleTypes.SHRIEK, "SHRIEK");
        register(ParticleTypes.EGG_CRACK, "EGG_CRACK");
    }

    public static String typeOf(ParticleType<?> type) {
        return PARTICLE_TYPE_MAP.get(type);
    }

    public static ParticleType<?> typeOf(String type) {
        return PARTICLE_TYPE_MAP.inverse().get(type);
    }
}
