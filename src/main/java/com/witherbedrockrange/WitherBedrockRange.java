package com.witherbedrockrange;

import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

/**
 * Forge 1.20.1 port of "Wither Bedrock Range".
 *
 * <p>This is a client-and-server mod that needs no configuration: all behaviour is
 * provided by {@link com.witherbedrockrange.mixin.WitherBossMixin}, which widens the
 * Wither's on-damage block-destruction area to match Bedrock Edition.
 */
@Mod(WitherBedrockRange.MOD_ID)
public class WitherBedrockRange {
    public static final String MOD_ID = "witherbedrockrange";

    private static final Logger LOGGER = LogUtils.getLogger();

    public WitherBedrockRange() {
        LOGGER.debug("{} loaded", MOD_ID);
    }
}