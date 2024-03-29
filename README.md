# Obzcure Virtual Machine
## Java virtual machine made in Java

# THIS IS NOT PRODUCTION SAFE - WORK IN PROGRESS!  
- *Use at your own risk.*
- Requires Java 17 (with preview features)

`java --enable-preview  -jar ObzcureVM.jar`  
`"path/to/java17" --enable-preview  -jar ObzcureVM.jar -i in.jar -o out.jar -fp -rf -rm -sd`  
`"C:\Program Files\Java\graalvm-ce-java17-21.3.0\bin\java.exe" --enable-preview  -jar ObzcureVM.jar -i in.jar -o out.jar -fp -rf -rm -sd`

The virtualized output jar also now requires Java 17 (with preview features)

```
Usage:
 -fp,--forcePublic      Make every field and method public (accessible
                        from everywhere)
 -i,--input <input>     Input jar file
 -o,--output <output>   Output jar file
 -rf,--removeFinal      Force virtualization of final fields (removes
                        final access)
 -rm,--rndMeow          Random cats.meow filename
 -sd,--skipDebug        Remove debugging information from all classes
```

### Before
![image](https://github.com/HoverCatz/ObzcureVirtualMachine/assets/1442391/af5419d7-a4fb-48c0-9c64-70c35c1c8e2a)

### After
![image](https://github.com/HoverCatz/ObzcureVirtualMachine/assets/1442391/137620a2-ca72-4a99-b253-896134f57d77)

## Example timings:
Virtualized output file:
- 5458 ms
- 5011 ms
- 4947 ms
- 4873 ms
- 4937 ms

Original input file:
- 53 ms
- 18 ms
- 21 ms
- 12 ms
- 9 ms

*As you can see from these 5 test runs, the virtualized version of a jar is up to 606 times slower than the original jar.  
This is a worst case scenario, where a huge jar was fully virtualized including lots of nested loops.  
If you only virtualize a few specific methods, it shouldn't be much slower than the original.*
