#!/bin/bash

# 徽章用户名替换脚本
# 在推送到GitHub前运行此脚本以替换徽章URL中的用户名

echo "========================================="
echo "徽章用户名替换脚本"
echo "========================================="
echo ""

# 检查是否提供了用户名
if [ -z "$1" ]; then
    echo "错误: 请提供GitHub用户名"
    echo ""
    echo "使用方法:"
    echo "  ./replace-badges.sh YOUR_GITHUB_USERNAME"
    echo ""
    echo "示例:"
    echo "  ./replace-badges.sh johndoe"
    exit 1
fi

USERNAME="$1"

echo "将替换徽章URL中的用户名为: $USERNAME"
echo ""

# 检查README.md是否存在
if [ ! -f "README.md" ]; then
    echo "错误: 找不到README.md文件"
    exit 1
fi

# 备份原始文件
cp README.md README.md.backup
echo "已创建备份: README.md.backup"
echo ""

# 替换README.md中的用户名
sed -i "s|YOUR_USERNAME|$USERNAME|g" README.md

echo "✅ 已替换README.md中的用户名"
echo ""

# 显示修改的内容
echo "修改的行:"
grep -n "github.com/$USERNAME/SkinToStatueAndroid" README.md
echo ""

# 检查是否还有未替换的占位符
REMAINING=$(grep -c "YOUR_USERNAME" README.md 2>/dev/null || echo "0")

if [ "$REMAINING" -gt 0 ]; then
    echo "⚠️  警告: 还有 $REMAINING 处未替换的占位符"
    echo ""
    echo "未替换的行:"
    grep -n "YOUR_USERNAME" README.md
else
    echo "✅ 所有占位符已替换完成"
fi

echo ""
echo "========================================="
echo "完成!"
echo "========================================="
echo ""
echo "下一步:"
echo "  1. 检查README.md确认替换正确"
echo "  2. 提交更改: git add README.md"
echo "  3. 提交: git commit -m 'docs: update badges with correct username'"
echo "  4. 推送: git push origin main"
echo ""
echo "如果需要恢复原始文件:"
echo "  cp README.md.backup README.md"
echo ""