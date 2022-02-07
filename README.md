# Obzcure Virtual Machine
## Java virtual machine made in Java

###### Requires Java 17 (with preview features)

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
![Before vm](https://cdn.discordapp.com/attachments/399714742765092864/932573252071620648/unknown.png)


### After
![After vm](https://cdn.discordapp.com/attachments/399714742765092864/932573279418454016/unknown.png)

## Example timings:
Virtualized output file:
- 5458 ms
- 5011 ms
- 4947 ms
- 4873 ms
- 4937 ms

Original output file:
- 53 ms
- 18 ms
- 21 ms
- 12 ms
- 9 ms

*As you can see from these 5 test runs, the virtualized version of a jar is up to 606 times slower than the original jar.*
