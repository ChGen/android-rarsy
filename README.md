# android-rarsy

This is android app to run simple RISC-V assembly programs on any Android device.  
It relies on [RARS Emulator](https://github.com/TheThirdOne/rars) code for RARS-V emulator implementation.
Note that my current implementation is very sketchy and code quality is low.

How to use:
1. Update git submodules:
```
git submodule update --init --checkout --recursive
```
2. Open `./Rarsy/` in Android Studio
3. Build and run android app. No NDK required.
4. Add provides ready-to-use snipped. Press run and enjoy RISC-V assembly!
