package com.witherbedrockrange.mixin;

import net.minecraft.world.entity.boss.wither.WitherBoss;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Forge 1.20.1: widens the Wither's on-damage block-destruction area from Java
 * Edition's hardcoded 3x4x3 box to Bedrock Edition's 4x6x4.
 *
 * <p>In 1.20.1, {@code WitherBoss.customServerAiStep()} destroys blocks using three
 * hard-coded nested loops (it does not use {@code BlockPos.betweenClosed} the way
 * newer versions do). Maintaining a {@code destroyBlocksTick} countdown, it clears:
 * <pre>
 * int y = Mth.floor(this.getY());
 * int x = Mth.floor(this.getX());
 * int z = Mth.floor(this.getZ());
 * for (int i = -1; i &lt;= 1; ++i)          // x offset: -1 .. +1  (3 wide)
 *   for (int j = -1; j &lt;= 1; ++j)        // z offset: -1 .. +1  (3 deep)
 *     for (int k = 0; k &lt;= 3; ++k)       // y offset:  0 .. +3  (4 tall)
 *       ... destroy new BlockPos(x+i, y+k, z+j) if destroyable ...
 * </pre>
 *
 * <p>We {@link ModifyConstant} the three relevant integer constants so the box becomes
 * 4 wide (X) x 6 tall (Y) x 4 deep (Z):
 * <ul>
 *   <li>the two {@code -1} lower bounds (the only two in this method) become {@code -2},
 *       turning X/Z into {@code -2 .. +1};</li>
 *   <li>the {@code 3} upper bound of the vertical loop (the only one in this method)
 *       becomes {@code 5}, turning Y into {@code 0 .. +5}.</li>
 * </ul>
 * The file is written against <em>official</em> (Mojang) names; the refmap
 * {@code witherbedrockrange.refmap.json} remaps them to SRG for the production jar.
 */
@Mixin(WitherBoss.class)
public abstract class WitherBossMixin {

    /** Bedrock Edition: wide the horizontal reach to -2 .. +1 (4 blocks). */
    @Unique
    private static final int BEDROCK_REACH = -2;
    /** Bedrock Edition: six blocks of vertical reach above the base (0 .. +5). */
    @Unique
    private static final int BEDROCK_HEIGHT = 5;

    /** X-offset loop lower bound: -1 -> -2. */
    @ModifyConstant(method = "customServerAiStep", constant = @Constant(intValue = -1, ordinal = 0))
    private int witherbedrockrange$behindX(int value) {
        return BEDROCK_REACH;
    }

    /** Z-offset loop lower bound: -1 -> -2. */
    @ModifyConstant(method = "customServerAiStep", constant = @Constant(intValue = -1, ordinal = 1))
    private int witherbedrockrange$behindZ(int value) {
        return BEDROCK_REACH;
    }

    /** Y-offset loop upper bound: 3 -> 5. (ordinal 1: the first int 3 in customServerAiStep
     *  is the head-array loop bound `i < 3`; the SECOND int 3 is the destroy-loop upper bound.) */
    @ModifyConstant(method = "customServerAiStep", constant = @Constant(intValue = 3, ordinal = 1))
    private int witherbedrockrange$up(int value) {
        return BEDROCK_HEIGHT;
    }
}