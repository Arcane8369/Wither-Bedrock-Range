# Wither Bedrock Range

一个 **NeoForge 1.21.1** 的模组，让凋灵（Wither）在**受伤时**对周围方块的破坏范围对齐**基岩版（Bedrock Edition）**。

- **Java 版官方行为**：受伤时约破坏 **3 × 4 × 3** 的方块区域
- **本模组（基岩版风格）**：受伤时破坏 **4 × 6 × 4** 的方块区域

本模组复刻了基岩版更具破坏力的凋灵：不再只是在被打时崩出一个小的方块坑，而是清掉明显更大的一个区域。

## 环境要求

- Minecraft Java 版 **1.21.1**
- **NeoForge** `21.1.x` 或更新版本
- 从源码编译需要 **Java 21**

## 安装方法

1. 下载模组 JAR（见发布页，或按下方「从源码构建」自行编译）。
2. 将 JAR 放入客户端 `.minecraft/mods/` 目录（和/或专用服务器的 `mods/` 目录）。
3. 启动游戏，召唤凋灵并攻击它，即可看到基岩版风格的破坏范围。

## 从源码构建

```bash
# Linux / macOS
./gradlew build

# Windows
gradlew.bat build
```

编译出的模组 JAR 位于：

```
build/libs/
```

### 调整破坏范围大小

控制范围大小的三个数值位于：

`src/main/java/com/witherbedrockrange/mixin/WitherBossMixin.java`

| 常量                  | 含义                                    | 默认值（基岩版） |
|-----------------------|-----------------------------------------|------------------|
| `BEDROCK_REACH_BEHIND`| 凋灵位置向水平负方向延伸的方块数          | `2`              |
| `BEDROCK_REACH_AHEAD` | 凋灵位置向水平正方向延伸的方块数          | `1`              |
| `BEDROCK_REACH_UP`    | 凋灵脚底向上延伸的方块数                  | `5`              |

这三个值组合起来即为 **4（X）× 6（Y）× 4（Z）** 的立方体范围。

## 实现原理

原版凋灵在 `WitherBoss.customServerAiStep()` 中，受伤后通过遍历
`BlockPos.betweenClosed(...)` 来破坏方块。本模组使用 **Mixin**（`@Redirect`）
将该调用替换为基岩版大小的范围。

## 项目结构

```
.
├── .github/workflows/          # CI：构建 + 游戏内运行时测试
├── build.gradle.kts            # 构建配置并集中管理模组元数据
├── gradle/wrapper/             # Gradle wrapper（可复现构建）
├── gradle.properties
├── settings.gradle.kts
└── src/main/
    ├── java/com/witherbedrockrange/
    │   ├── WitherBedrockRange.java       # 模组入口
    │   └── mixin/WitherBossMixin.java    # 破坏范围 Mixin
    └── resources/
        └── witherbedrockrange.mixins.json
```

## 署名与 AI 编写声明

**作者：Arcane、DeepSeek-V4**

**重要声明（请务必阅读）：**

> 本项目由 **AI（DeepSeek-V4）自动生成**，其后的修改与维护也主要由 AI 完成。
> 项目提供时 **按原样（"AS IS"）交付，不对代码质量、正确性、稳定性、安全性或
> 与任何第三方软件的兼容性做任何保证**。请在使用前自行审查代码、充分测试，
> 并自行承担使用本项目可能产生的全部风险。

## 许可证

本项目基于 [GNU General Public License v3.0](LICENSE.txt) 协议发布。