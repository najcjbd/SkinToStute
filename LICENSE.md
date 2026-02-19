# License

This project is licensed under the **GNU General Public License v3.0**.

## Why GPL v3.0?

This project includes code ported from **PlayerStatueBuilder**, which is licensed under GPL v3.0.

According to **GPL v3.0 Section 2(b)**:

> "You must license the entire work, as a whole, under this License to anyone who comes into possession of a copy."

Because this work contains GPL-3.0 licensed code, the entire work must be licensed under GPL v3.0.

## Ported Code

The following components are based on or inspired by PlayerStatueBuilder:

| Component | Ported From | Description |
|-----------|-------------|-------------|
| ColorMatcher.kt | ColorDiffs.java | Color difference algorithms (RGB, ABSRGB, HSL, HSB, LAB) |
| ImageFilters.kt | ImageUtil.java | Image processing filters (hue, saturation, brightness, contrast) |
| SchematicGenerator.kt | FaceBuilder.java | Sponge Schematic coordinate transformation |
| LitematicaGenerator.kt | FaceBuilder.java | Litematic coordinate transformation |
| SkinProcessor.kt | LegacyConverter.java | Legacy skin conversion logic |

## Original Project

**PlayerStatueBuilder**
- License: GNU General Public License v3.0
- Author: phoenixr-codes
- Repository: https://github.com/phoenixr-codes/PlayerStatueBuilder

## Your Rights

Under GPL v3.0, you have the right to:

✅ **Use** - Use this software for any purpose (including commercial)
✅ **Study** - Study how the software works and change it
✅ **Redistribute** - Redistribute copies of the software
✅ **Modify** - Distribute copies of your modified versions

## Your Responsibilities

Under GPL v3.0, you must:

⚠️ **Share Source Code** - Provide source code when you distribute the software
⚠️ **License Modifications** - License your modifications under GPL v3.0 or later
⚠️ **Include License** - Include a copy of the GPL v3.0 license
⚠️ **Preserve Notices** - Keep all copyright and license notices intact

## What This Means

### For Users

You can freely use this software for any purpose, including commercial projects.

### For Developers

If you modify or distribute this software:

1. **You must share your source code** - When you distribute the software (even in compiled form), you must make the source code available

2. **Your modifications must be GPL v3.0** - Any modifications you make must also be licensed under GPL v3.0 or later

3. **You cannot combine with proprietary software** - You cannot combine this software with proprietary code in a way that would make the combined work non-free

4. **You must preserve attribution** - You must keep all copyright and license notices, including attribution to PlayerStatueBuilder

## Commercial Use

Yes, you **can** use this software in commercial projects!

However, if you distribute your commercial product that includes this software:

- You must make your modifications available under GPL v3.0
- You cannot keep your modifications private
- Your customers have the same rights to use, modify, and redistribute the software

This is the **copyleft** principle: freedom is preserved by requiring that modifications are shared back to the community.

## Third-Party Libraries

This project uses the following third-party libraries with their respective licenses:

| Library | License | Purpose |
|---------|---------|---------|
| AndroidX | Apache License 2.0 | Android framework libraries |
| Kotlin | Apache License 2.0 | Kotlin standard library |
| Compose | Apache License 2.0 | UI framework |
| OkHttp | Apache License 2.0 | HTTP client |
| Gson | Apache License 2.0 | JSON parsing |
| jNBT | MIT License | NBT file format handling |
| Coil | Apache License 2.0 | Image loading |
| MockK | Apache License 2.0 | Testing framework |

These libraries are separate works and are **not** covered by this GPL license. They are used as system libraries or through their own compatible licenses.

## How to Comply

If you want to use or modify this software:

1. **Include the LICENSE file** - Keep a copy of the GPL v3.0 license with your distribution

2. **Provide source code** - When distributing the software, provide a way for recipients to get the source code:
   - Include it with your distribution, OR
   - Provide a written offer to send it, OR
   - Host it on a public server

3. **License your changes** - License any modifications you make under GPL v3.0 or later

4. **Preserve attribution** - Keep all copyright notices and license text intact

5. **Acknowledge PlayerStatueBuilder** - Include attribution to the original PlayerStatueBuilder project

## Full License Text

The complete GNU General Public License v3.0 text is included in the `LICENSE` file in the root of this repository.

You can also read it at:
- https://www.gnu.org/licenses/gpl-3.0.txt
- https://www.gnu.org/licenses/gpl-faq.html (FAQ)

## Questions?

If you have questions about this license:

- Read the [GPL FAQ](https://www.gnu.org/licenses/gpl-faq.html)
- Consult a lawyer for legal advice
- Submit an issue if you need clarification about this project's licensing

## Summary

| What you CAN do | What you MUST do |
|----------------|------------------|
| ✅ Use for any purpose | ⚠️ Share source code when distributing |
| ✅ Study and modify | ⚠️ License modifications as GPL v3.0 |
| ✅ Redistribute copies | ⚠️ Include the GPL v3.0 license |
| ✅ Use commercially | ⚠️ Preserve all copyright notices |
| | ⚠️ Acknowledge PlayerStatueBuilder |

**The GPL v3.0 ensures that this software and its derivatives remain free and open source for everyone.**