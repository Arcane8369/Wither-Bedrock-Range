package com.witherbedrockrange.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;

/**
 * Widens the Wither's block-destruction area (the one that triggers every time the
 * Wither takes damage) from Java Edition's 3x~5x3 footprint to Bedrock Edition's 4x6x4.
 *
 * <p>In {@code WitherBoss.customServerAiStep()} the vanilla code, after being hurt,
 * iterates over a box centered around the Wither and destroys destroyable blocks:
 * <pre>
 * int i = Mth.floor(this.getBbWidth() / 2.0F + 1.0F); // -> 1  (X/Z: -1 .. +1, width 3)
 * int j = Mth.floor(this.getBbHeight());              // -> 4  (Y: base .. +4, height 5)
 * for (BlockPos p : BlockPos.betweenClosed(
 *          bx - i, by, bz - i, bx + i, by + j, bz + i)) { ... }
 * </pre>
 *
 * <p>We {@link Redirect} that single {@link BlockPos#betweenClosed} call so the box
 * becomes 4 wide (X) x 6 tall (Y) x 4 deep (Z), anchored the same way as vanilla
 * (horizontal extent measured from the Wither's block position, extending upward),
 * matching the far more destructive Bedrock Edition behaviour.
 */
@Mixin(WitherBoss.class)
public abstract class WitherBossMixin {

    /** Bedrock Edition: horizontal reach behind / below the Wither's origin. */
    @Unique
    private static final int BEDROCK_REACH_BEHIND = 2;
    /** Bedrock Edition: horizontal reach in front of / in the positive direction. */
    @Unique
    private static final int BEDROCK_REACH_AHEAD = 1;
    /** Bedrock Edition: how high above the Wither's base the box extends. */
    @Unique
    private static final int BEDROCK_REACH_UP = 5;

    @Redirect(
        method = "customServerAiStep",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/BlockPos;betweenClosed(IIIIII)Ljava/lang/Iterable;"
        )
    )
    private Iterable<BlockPos> witherbedrockrange$expandDestructionArea(
            int originalX1, int originalY1, int originalZ1, int originalX2, int originalY2, int originalZ2) {
        WitherBoss wither = (WitherBoss) (Object) this;
        int bx = wither.getBlockX();
        int by = wither.getBlockY();
        int bz = wither.getBlockZ();
        // X: bx-2 .. bx+1 (4 blocks), Y: by .. by+5 (6 blocks), Z: bz-2 .. bz+1 (4 blocks).
        return BlockPos.betweenClosed(
            bx - BEDROCK_REACH_BEHIND, by, bz - BEDROCK_REACH_BEHIND,
            bx + BEDROCK_REACH_AHEAD, by + BEDROCK_REACH_UP, bz + BEDROCK_REACH_AHEAD
        );
    }
}