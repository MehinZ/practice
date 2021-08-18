# GC回收堆内存分析

## 运行环境

物理内存: 8G

核数: 4

JVM堆内存设置: -Xmx1g -Xms1g

## 串行GC(Serial GC)

GC设置参数: -XX:+UseSerialGC

```
正在执行...
2021-08-16T09:08:03.577+0800: [GC (Allocation Failure) 2021-08-16T09:08:03.578+0800: [DefNew: 279616K->34944K(314560K), 0.0499453 secs] 279616K->88094K(1013632K), 0.0502936 secs] [Times: user=0.01 sys=0.03, real=0.05 secs] 
2021-08-16T09:08:03.670+0800: [GC (Allocation Failure) 2021-08-16T09:08:03.670+0800: [DefNew: 314560K->34943K(314560K), 0.0593108 secs] 367710K->163901K(1013632K), 0.0593520 secs] [Times: user=0.03 sys=0.03, real=0.06 secs] 
2021-08-16T09:08:03.766+0800: [GC (Allocation Failure) 2021-08-16T09:08:03.766+0800: [DefNew: 314559K->34943K(314560K), 0.0468289 secs] 443517K->244394K(1013632K), 0.0468631 secs] [Times: user=0.02 sys=0.01, real=0.05 secs] 
2021-08-16T09:08:03.851+0800: [GC (Allocation Failure) 2021-08-16T09:08:03.851+0800: [DefNew: 314559K->34943K(314560K), 0.0477801 secs] 524010K->322730K(1013632K), 0.0478490 secs] [Times: user=0.03 sys=0.02, real=0.05 secs] 
2021-08-16T09:08:03.938+0800: [GC (Allocation Failure) 2021-08-16T09:08:03.938+0800: [DefNew: 314559K->34944K(314560K), 0.0492691 secs] 602346K->402413K(1013632K), 0.0493064 secs] [Times: user=0.03 sys=0.02, real=0.05 secs] 
2021-08-16T09:08:04.026+0800: [GC (Allocation Failure) 2021-08-16T09:08:04.026+0800: [DefNew: 314560K->34943K(314560K), 0.0444266 secs] 682029K->473147K(1013632K), 0.0444639 secs] [Times: user=0.02 sys=0.03, real=0.04 secs] 
2021-08-16T09:08:04.111+0800: [GC (Allocation Failure) 2021-08-16T09:08:04.111+0800: [DefNew: 314559K->34943K(314560K), 0.0582648 secs] 752763K->545641K(1013632K), 0.0583912 secs] [Times: user=0.05 sys=0.02, real=0.06 secs] 
2021-08-16T09:08:04.208+0800: [GC (Allocation Failure) 2021-08-16T09:08:04.208+0800: [DefNew: 314559K->34944K(314560K), 0.0986193 secs] 825257K->624879K(1013632K), 0.0986554 secs] [Times: user=0.06 sys=0.03, real=0.10 secs] 
2021-08-16T09:08:04.357+0800: [GC (Allocation Failure) 2021-08-16T09:08:04.357+0800: [DefNew: 314560K->34942K(314560K), 0.6313025 secs] 904495K->706982K(1013632K), 0.6313421 secs] [Times: user=0.03 sys=0.59, real=0.63 secs] 
执行结束!共生成对象次数:9436
Heap
 def new generation   total 314560K, used 46518K [0x00000000c0000000, 0x00000000d5550000, 0x00000000d5550000)
  eden space 279616K,   4% used [0x00000000c0000000, 0x00000000c0b4ddc8, 0x00000000d1110000)
  from space 34944K,  99% used [0x00000000d3330000, 0x00000000d554fb88, 0x00000000d5550000)
  to   space 34944K,   0% used [0x00000000d1110000, 0x00000000d1110000, 0x00000000d3330000)
 tenured generation   total 699072K, used 672039K [0x00000000d5550000, 0x0000000100000000, 0x0000000100000000)
   the space 699072K,  96% used [0x00000000d5550000, 0x00000000fe599e48, 0x00000000fe59a000, 0x0000000100000000)
 Metaspace       used 3262K, capacity 4556K, committed 4864K, reserved 1056768K
  class space    used 341K, capacity 392K, committed 512K, reserved 1048576K
```

第一次GC在`2021-08-16T09:08:03.577`发生，GC产生的原因是内存分配失败（`Allocation Failure`）

在总的`314560K(307.1875M)`年轻代中，将年轻代内存从`279616K(273.0625M`)降低到`34944K(34.125M)`，共降低`238.9375M`内存，GC持续时间为50ms。

此次GC将堆内存的使用大小从`279616K(273.0625M)`减低到`88094K（86.029M）`，其中堆内存的总大小为`1013632K(989.875M)`

可以看出此次GC有51.9M(86.029-34.125)对象数据晋升到了老年代。

在此次GC前，年轻代的使用率为88.9%，GC后使用率为11.1%。

其他GC的分析类似上述分析。

---

**总结：** 从以上GC日志可以看出，共进行了9次GC，均是MinorGC，在当前配置下，没有发生MajorGC(FullGC)，说明老年代内存没有达到回收阈值。

9次MinorGC的平均持续时间为53.5ms

## 并行GC(Parallel GC)

```
正在执行...
2021-08-18T10:30:46.415+0800: [GC (Allocation Failure) [PSYoungGen: 262144K->43518K(305664K)] 262144K->73513K(1005056K), 0.0254523 secs] [Times: user=0.03 sys=0.05, real=0.03 secs] 
2021-08-18T10:30:46.490+0800: [GC (Allocation Failure) [PSYoungGen: 305662K->43509K(305664K)] 335657K->145601K(1005056K), 0.0308843 secs] [Times: user=0.02 sys=0.08, real=0.03 secs] 
2021-08-18T10:30:46.568+0800: [GC (Allocation Failure) [PSYoungGen: 305653K->43509K(305664K)] 407745K->215970K(1005056K), 0.0277656 secs] [Times: user=0.03 sys=0.05, real=0.03 secs] 
2021-08-18T10:30:46.642+0800: [GC (Allocation Failure) [PSYoungGen: 305653K->43518K(305664K)] 478114K->297216K(1005056K), 0.0354254 secs] [Times: user=0.11 sys=0.06, real=0.04 secs] 
2021-08-18T10:30:46.724+0800: [GC (Allocation Failure) [PSYoungGen: 305074K->43512K(305664K)] 558772K->378224K(1005056K), 0.0263088 secs] [Times: user=0.05 sys=0.06, real=0.03 secs] 
2021-08-18T10:30:46.791+0800: [GC (Allocation Failure) [PSYoungGen: 305656K->43513K(160256K)] 640368K->456867K(859648K), 0.0278552 secs] [Times: user=0.05 sys=0.14, real=0.03 secs] 
2021-08-18T10:30:46.838+0800: [GC (Allocation Failure) [PSYoungGen: 160123K->70188K(232960K)] 573478K->490446K(932352K), 0.0135060 secs] [Times: user=0.09 sys=0.00, real=0.01 secs] 
2021-08-18T10:30:46.872+0800: [GC (Allocation Failure) [PSYoungGen: 186924K->97472K(232960K)] 607182K->527336K(932352K), 0.0186471 secs] [Times: user=0.03 sys=0.00, real=0.02 secs] 
2021-08-18T10:30:46.912+0800: [GC (Allocation Failure) [PSYoungGen: 214208K->116191K(232960K)] 644072K->562151K(932352K), 0.0225026 secs] [Times: user=0.11 sys=0.00, real=0.02 secs] 
2021-08-18T10:30:46.954+0800: [GC (Allocation Failure) [PSYoungGen: 232927K->79967K(232960K)] 678887K->591747K(932352K), 0.0263615 secs] [Times: user=0.09 sys=0.03, real=0.03 secs] 
2021-08-18T10:30:47.008+0800: [GC (Allocation Failure) [PSYoungGen: 196677K->42103K(232960K)] 708457K->624531K(932352K), 0.0282916 secs] [Times: user=0.06 sys=0.05, real=0.03 secs] 
2021-08-18T10:30:47.037+0800: [Full GC (Ergonomics) [PSYoungGen: 42103K->0K(232960K)] [ParOldGen: 582427K->346423K(699392K)] 624531K->346423K(932352K), [Metaspace: 3255K->3255K(1056768K)], 0.0638767 secs] [Times: user=0.27 sys=0.00, real=0.06 secs] 
2021-08-18T10:30:47.126+0800: [GC (Allocation Failure) [PSYoungGen: 116736K->39979K(232960K)] 463159K->386402K(932352K), 0.0077684 secs] [Times: user=0.08 sys=0.00, real=0.01 secs] 
2021-08-18T10:30:47.156+0800: [GC (Allocation Failure) [PSYoungGen: 156715K->35450K(232960K)] 503138K->417229K(932352K), 0.0115458 secs] [Times: user=0.08 sys=0.00, real=0.01 secs] 
2021-08-18T10:30:47.192+0800: [GC (Allocation Failure) [PSYoungGen: 152186K->42650K(232960K)] 533965K->456898K(932352K), 0.0153237 secs] [Times: user=0.06 sys=0.00, real=0.02 secs] 
执行结束!共生成对象次数:9439
Heap
 PSYoungGen      total 232960K, used 48041K [0x00000000eab00000, 0x0000000100000000, 0x0000000100000000)
  eden space 116736K, 4% used [0x00000000eab00000,0x00000000eb043e48,0x00000000f1d00000)
  from space 116224K, 36% used [0x00000000f8e80000,0x00000000fb8268b8,0x0000000100000000)
  to   space 116224K, 0% used [0x00000000f1d00000,0x00000000f1d00000,0x00000000f8e80000)
 ParOldGen       total 699392K, used 414248K [0x00000000c0000000, 0x00000000eab00000, 0x00000000eab00000)
  object space 699392K, 59% used [0x00000000c0000000,0x00000000d948a2d8,0x00000000eab00000)
 Metaspace       used 3262K, capacity 4556K, committed 4864K, reserved 1056768K
  class space    used 341K, capacity 392K, committed 512K, reserved 1048576K
```

### MinorGC

第一次GC在`2021-08-18T10:30:46.415+0800`发生，GC产生的原因是内存分配失败（`Allocation Failure`）

在总的`305664K(298.5M)`年轻代内存中，将年轻代内存从`262144K(256M`)降低到`43518K(42.49M)`，共降低`213.5M`内存，GC持续时间为25.4ms。

此次GC将堆内存的使用大小从`262144(256M)`减低到`73513K（71.79M）`

可以看出此次GC有`29.29M((73513-43518)/1024)M`对象数据晋升到了老年代。

在此次GC前，年轻代的使用率为`85.76%`，GC后使用率为`14.24%`。

其中，`user=0.03 sys=0.05, real=0.03`中的real表示GC实际执行的时间，约为30ms左右，比实际的串行GC速度有所提升。

其他MinorGC的分析类似上述分析。

### MajorGC（FullGC）

在`2021-08-18T10:30:47.037+0800`发生了一次FullGC，`Ergonomics`表示老年代内存达到了触发MajorGC的阈值，

ParallelGC使用了PSYoungGen的STW收集器，将年轻代内存从`42103K`降低到0

此次GC将老年代的内存从`582427K(568.77M)`回收到`346423K(338.3M)`
在GC之前，老年代的使用率为`83.27%(582427/699392)`
GC之后，老年代的使用率为`49.5%(346423/699392)`。

FullGC之后，堆内存的使用率从67%降低到37%，堆内存占用有明显降低。

---

**总结：** 从以上GC日志可以看出，共进行了15次GC，其中有14次MinorGC，1次MajorGC，频率明显比串行GC要高，但是平均每次GC执行时间较短，为22ms。

## 并发标记清除GC(CMS)

```
正在执行...
2021-08-18T11:48:37.458+0800: [GC (Allocation Failure) 2021-08-18T11:48:37.459+0800: [ParNew: 279616K->34944K(314560K), 0.0214510 secs] 279616K->85452K(1013632K), 0.0219196 secs] [Times: user=0.02 sys=0.09, real=0.02 secs] 
2021-08-18T11:48:37.523+0800: [GC (Allocation Failure) 2021-08-18T11:48:37.523+0800: [ParNew: 314500K->34944K(314560K), 0.0994899 secs] 365009K->163210K(1013632K), 0.0998904 secs] [Times: user=0.05 sys=0.61, real=0.10 secs] 
2021-08-18T11:48:37.777+0800: [GC (Allocation Failure) 2021-08-18T11:48:37.778+0800: [ParNew: 314560K->34944K(314560K), 0.1467114 secs] 442826K->247819K(1013632K), 0.1476378 secs] [Times: user=0.72 sys=0.11, real=0.15 secs] 
2021-08-18T11:48:37.997+0800: [GC (Allocation Failure) 2021-08-18T11:48:37.997+0800: [ParNew: 314560K->34944K(314560K), 0.0625548 secs] 527435K->331020K(1013632K), 0.0626266 secs] [Times: user=0.14 sys=0.06, real=0.06 secs] 
2021-08-18T11:48:38.119+0800: [GC (Allocation Failure) 2021-08-18T11:48:38.119+0800: [ParNew: 314560K->34944K(314560K), 0.1687723 secs] 610636K->416441K(1013632K), 0.1688408 secs] [Times: user=0.53 sys=0.19, real=0.17 secs] 
2021-08-18T11:48:38.288+0800: [GC (CMS Initial Mark) [1 CMS-initial-mark: 381497K(699072K)] 422513K(1013632K), 0.0007196 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2021-08-18T11:48:38.290+0800: [CMS-concurrent-mark-start]
执行结束!共生成对象次数:5232
2021-08-18T11:48:38.298+0800: [CMS-concurrent-mark: 0.009/0.009 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
2021-08-18T11:48:38.298+0800: [CMS-concurrent-preclean-start]
2021-08-18T11:48:38.300+0800: [CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2021-08-18T11:48:38.300+0800: [CMS-concurrent-abortable-preclean-start]
Heap
 par new generation   total 314560K, used 46608K [0x00000000c0000000, 0x00000000d5550000, 0x00000000d5550000)
  eden space 279616K,   4% used [0x00000000c0000000, 0x00000000c0b64248, 0x00000000d1110000)
  from space 34944K, 100% used [0x00000000d3330000, 0x00000000d5550000, 0x00000000d5550000)
  to   space 34944K,   0% used [0x00000000d1110000, 0x00000000d1110000, 0x00000000d3330000)
 concurrent mark-sweep generation total 699072K, used 381497K [0x00000000d5550000, 0x0000000100000000, 0x0000000100000000)
 Metaspace       used 3262K, capacity 4556K, committed 4864K, reserved 1056768K
  class space    used 341K, capacity 392K, committed 512K, reserved 1048576K
```

### MinorGC

第一次MinorGC在`2021-08-18T11:48:37.458`发生，GC产生的原因是内存分配失败（`Allocation Failure`）

在总的`314560K(307.1875M)`年轻代内存中，将年轻代内存从`279616K`降低到`34944K`，共降低`238.9375M`内存，GC持续时间为21.4ms。

此次GC将堆内存的使用大小从`279616K`减低到`85452K`

其中的差值表示此次MinorGC有`49.32M((85452-34944)/1024)M`对象数据晋升到了老年代。

在此次GC前，年轻代的使用率为`88.9%`，GC后使用率为`11.1%`。 整体堆内存的使用率从`27.6%`降低到了`8.4%`
其他MinorGC的分析类似上述分析。

### MajorGC(FullGC)

CMS的MajorGC有以下6个阶段

#### 1. 初始标记（Initial Mark）[会发生STW]

初始标记目标是标记所有的根对象，为下一阶段遍历引用做准备

```
2021-08-18T13:45:14.774+0800: [GC (CMS Initial Mark) [1 CMS-initial-mark: 376104K(699072K)] 411334K(1013632K), 0.0002663 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
```

其中可以看到，标记时老年代的使用量为`376104K`，占总老年代`699072K`的`53.8%`，运行时间极短可以忽略不计。

#### 2. 并发标记（Concurrent Mark）[不会STW]

通过第一阶段找到的根对象开始，并发遍历并标记所有对象。

```
2021-08-18T13:45:14.774+0800: [CMS-concurrent-mark-start]
2021-08-18T13:45:14.779+0800: [CMS-concurrent-mark: 0.005/0.005 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
```

日志表示并发标记共消耗了5ms`(0.005/0.005 secs)`表示GC本身线程消耗时间与总消耗时间

#### 3. 并发预清理(Concurrent Preclean)[不会STW]

由于第二阶段也是并发执行，所以这个阶段会进行预清理，会统计前面的并发标记阶段执行过程中发生了改变的对象

```
2021-08-18T13:45:14.779+0800: [CMS-concurrent-preclean-start]
2021-08-18T13:45:14.782+0800: [CMS-concurrent-preclean: 0.003/0.003 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
```

日志表示并发标记共消耗了3ms`(0.003/0.003 secs)`表示GC本身线程消耗时间与总消耗时间

#### 4. 可取消的并发预清理(Concurrent Abortable Preclean)[不会STW]
```
2021-08-18T13:45:14.782+0800: [CMS-concurrent-abortable-preclean-start]
... 中间发生了一次MinorGC
2021-08-18T13:45:14.888+0800: [CMS-concurrent-abortable-preclean: 0.002/0.105 secs] [Times: user=0.28 sys=0.03, real=0.11 secs]
```
在STW之前循环并发清理并发GC期间的对象引用变动

#### 5. 最终标记(Final Remark)[会发生STW]
通过前面的预清理处理后，进行最后一次STW，进行最终存活对象的标记
```
2021-08-18T13:45:14.894+0800: 
[GC (CMS Final Remark) 
    [YG occupancy: 35015 K (314560 K)]
    2021-08-18T13:45:14.894+0800:
        [Rescan (parallel) , 0.0004767 secs]
        2021-08-18T13:45:14.894+0800: [weak refs processing, 0.0000250 secs]
        2021-08-18T13:45:14.894+0800: [class unloading, 0.0007812 secs]
        2021-08-18T13:45:14.895+0800: [scrub symbol table, 0.0012728 secs]
        2021-08-18T13:45:14.896+0800: [scrub string table, 0.0002977 secs]
        [1 CMS-remark: 462858K(699072K)]
497873K(1013632K), 0.0030276 secs]
[Times: user=0.02 sys=0.00, real=0.00 secs]
```
- **YG occupancy**: 表示年轻代当前使用了`35015K`（总量为`314560 K`） 
- **Rescan (parallel)**: 表示在STW之后重新执行一次扫描并标记存活对象，该过程并行执行，执行时间为 `0.4ms`
- **weak refs processing**: 表示处理弱引用，执行时间为 `0.025ms`
- **class unloading**: 卸载不需要的类，执行时间为 `0.7812ms`
- **scrub symbol table**: 清理符号表，执行时间为 `1.27ms`
- **scrub string table**: 清理内联字符串对应的 string tables, 执行时间为 `0.3ms`
- **1 CMS-remark**: 表示一轮标记完成时，老年代的大小，为 `462858K`，比最初标记时的`376104K`增加了`86754K`（在此期间还会发生OOM吗？）

在标记执行后，堆内存的使用大小为 `497873K(486.2M)`

#### 6. 并发清除(Concurrent Sweep)[不会发生STW]
经过上述几个阶段的标记，这个阶段开始清除无效对象回收内存
```
2021-08-18T13:45:14.897+0800: [CMS-concurrent-sweep-start]
2021-08-18T13:45:14.899+0800: [CMS-concurrent-sweep: 0.003/0.003 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
```

#### 7. 并发重置(Concurrent Reset)[不会发生STW]
完成GC，重置CMS的内部标记状态准备下一次MajorGC
```
2021-08-18T13:45:14.899+0800: [CMS-concurrent-reset-start]
2021-08-18T13:45:14.903+0800: [CMS-concurrent-reset: 0.004/0.004 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
```

---
**总结：** CMS实现中MajorGC总体看停顿时间非常短，只有2次STW，而且STW的持续时间也非常短，但是由于CMS在MajorGC期间的多次并行
使得GC运行时需要多次并较长时间的标记，而且标记会不断变化，导致最终标记时的对象相对不够整齐，相比串行、并行GC来说CMS进行MajorGC后内存碎片化程度较高

## 垃圾优先GC(G1)
G1全程为Garbage-First，是将堆内存分为多个Region进行管理的GC实现，哪个Region的垃圾最多就会优先进行回收。

由于G1的垃圾回收机制主要分为`Young GC`和`Mixed GC`两种，且中间会穿插各类子任务，所以以下直接根据日志内容进行分析，先取其中一次YoungGC的日志：
### YoungGC
```
...
2021-08-18T18:08:36.134+0800: [GC pause (G1 Evacuation Pause) (young), 0.0024624 secs]
   [Parallel Time: 1.9 ms, GC Workers: 8]
      [GC Worker Start (ms): Min: 1322.0, Avg: 1322.1, Max: 1322.2, Diff: 0.2]
      [Ext Root Scanning (ms): Min: 0.0, Avg: 0.1, Max: 0.1, Diff: 0.1, Sum: 0.8]
      [Update RS (ms): Min: 0.3, Avg: 0.6, Max: 1.6, Diff: 1.4, Sum: 5.1]
      [Processed Buffers: Min: 0, Avg: 4.4, Max: 7, Diff: 7, Sum: 35]
      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Object Copy (ms): Min: 0.0, Avg: 1.0, Max: 1.3, Diff: 1.3, Sum: 8.0]
      [Termination (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.1, Sum: 0.2]
      [Termination Attempts: Min: 1, Avg: 1.0, Max: 1, Diff: 0, Sum: 8]
      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
      [GC Worker Total (ms): Min: 1.7, Avg: 1.8, Max: 1.8, Diff: 0.2, Sum: 14.2]
      [GC Worker End (ms): Min: 1323.8, Avg: 1323.9, Max: 1323.9, Diff: 0.0]
   [Code Root Fixup: 0.0 ms]
   [Code Root Purge: 0.0 ms]
   [Clear CT: 0.1 ms]
   [Other: 0.5 ms]
      [Choose CSet: 0.0 ms]
      [Ref Proc: 0.1 ms]
      [Ref Enq: 0.0 ms]
      [Redirty Cards: 0.1 ms]
      [Humongous Register: 0.1 ms]
      [Humongous Reclaim: 0.0 ms]
      [Free CSet: 0.0 ms]
   [Eden: 20480.0K(20480.0K)->0.0B(17408.0K) Survivors: 2048.0K->3072.0K Heap: 307.3M(400.0M)->291.1M(400.0M)]
 [Times: user=0.00 sys=0.00, real=0.00 secs] 
 ...
```

`[GC pause (G1 Evacuation Pause) (young), 0.0024624 secs]` 表示此次GC暂停是G1垃圾回收器在进行年轻代Region对象的转移而触发，即是普通的MinorGC，耗时 `2.4ms`

> 当年轻代空间用满后，应用线程会被暂停，年轻代内存块中的存活对象被拷贝到存活区。拷贝的过程称为转移(Evacuation)

`[Parallel Time: 1.9 ms, GC Workers: 8]`: 表示本次GC的总耗时为`1.9ms`和总工作线程数为`8`

后续的内容为各类工作线程的耗时统计：
> `[GC Worker Start (ms): Min: 1322.0, Avg: 1322.1, Max: 1322.2, Diff: 0.2]`: 表示GC线程启动与暂定开始时间的差值情况，若最大最小差值较大，表示JVM整体线程比较繁忙，CPU分配GC的时间片较少
>
> `[Ext Root Scanning (ms): Min: 0.0, Avg: 0.1, Max: 0.1, Diff: 0.1, Sum: 0.8]`: 堆外内存对象状态的扫描时间
> 
>     [Update RS (ms): Min: 0.3, Avg: 0.6, Max: 1.6, Diff: 1.4, Sum: 5.1]:
>     [Processed Buffers: Min: 0, Avg: 4.4, Max: 7, Diff: 7, Sum: 35]: 
>     [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]:
>     表示处理RSet内容的工作线程包含扫描和更新Region的RSet
> 
> `[Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]`: 扫描应用代码中的根对象耗时 
> 
> `[Object Copy (ms): Min: 0.0, Avg: 1.0, Max: 1.3, Diff: 1.3, Sum: 8.0]`: 拷贝存活对象耗时
> 
> `[Termination (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.1, Sum: 0.2]`: 等待中止的等待耗时（实际是主Worker的总执行时间？）
> 
> `[Termination Attempts: Min: 1, Avg: 1.0, Max: 1, Diff: 0, Sum: 8]`: 尝试结束主GC线程耗时
> 
> `[GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]`: 其他工作线程耗时
> 
> `[GC Worker Total (ms): Min: 1.7, Avg: 1.8, Max: 1.8, Diff: 0.2, Sum: 14.2]`: GC所有工作的总耗时
> 
> `[GC Worker End (ms): Min: 1323.8, Avg: 1323.9, Max: 1323.9, Diff: 0.0]`: GC的worker线程完成作业时刻(相对GC暂停开始时间)，相对于此次GC暂停开始时间的毫秒数。


其他任务耗时详情
```
[Other: 0.5 ms]:总耗时0.5ms
      [Choose CSet: 0.0 ms]:选择CSet耗时
      [Ref Proc: 0.1 ms]: 处理非强引用对象耗时
      [Ref Enq: 0.0 ms]: 处理非强引用耗时
      [Redirty Cards: 0.1 ms]: 重新扫描Dirty Card耗时
      [Humongous Register: 0.1 ms]: H大对象注册与分配耗时
      [Humongous Reclaim: 0.0 ms]: H大对象声明耗时
      [Free CSet: 0.0 ms]: 释放清理CSet耗时
```

此次GC将Eden区从100%占用(20480K)清理到0%，整个堆内存从307.3M降低到291.1M，持续时间不足1s

### 并发标记（Concurrent Marking）
当堆内存的总体使用比例达到一定数值时，就会触发并发标记。
默认阈值比例是45%，可以通过`InitiatingHeapOccupancyPercent`设置阈值比例。

G1的并发标记分为以下5个阶段：
#### 1. 初始标记(Initial Mark)
```
2021-08-18T18:08:36.172+0800: [GC pause (G1 Humongous Allocation) (young) (initial-mark), 0.0011785 secs]
```
日志表示大对象分配引发了初始标记，耗时约`1.2ms`

#### 2. 并发Root区扫描(Root Region Scan)
```
2021-08-18T18:08:36.173+0800: [GC concurrent-root-region-scan-start]
2021-08-18T18:08:36.173+0800: [GC concurrent-root-region-scan-end, 0.0000736 secs]
```
此阶段标记所有从 "根区域" 可达的存活对象。 

**根区域：** 非空的区域，以及在标记过程中不得不收集的区域

#### 3. 并发标记(Concurrent Mark)
```
2021-08-18T18:08:36.173+0800: [GC concurrent-mark-start]
2021-08-18T18:08:36.176+0800: [GC concurrent-mark-end, 0.0028885 secs]
```

#### 4. 重新标记(Remark)
```
2021-08-18T18:08:36.176+0800:
[GC remark 2021-08-18T18:08:36.176+0800:
    [Finalize Marking, 0.0001079 secs]
        2021-08-18T18:08:36.176+0800: [GC ref-proc, 0.0005798 secs]
        2021-08-18T18:08:36.177+0800: [Unloading, 0.0009128 secs],
    0.0022837 secs]
[Times: user=0.00 sys=0.02, real=0.00 secs] 
 ```

#### 5. 清理(Cleanup)
```
2021-08-18T18:08:36.179+0800: [GC cleanup 306M->306M(400M), 0.0006757 secs]
 [Times: user=0.00 sys=0.00, real=0.00 secs] 
 ```
表示清理阶段，会清理空region（所以回收前后内存变化很小，几乎没有？），并会在这个阶段计算region的存活率，方便后续的其他GC。


### MixedGC(混合模式转移暂停)
Mixed的转移暂停会同时回收部分老年代的Region，有别于FullGC
发生在并发标记之后，期间可能还有多次YoungGC
```
2021-08-18T18:08:36.188+0800: [GC pause (G1 Evacuation Pause) (mixed), 0.0065874 secs]
   [Parallel Time: 5.8 ms, GC Workers: 8]
      [GC Worker Start (ms): Min: 1375.3, Avg: 1375.5, Max: 1376.0, Diff: 0.7]
      [Ext Root Scanning (ms): Min: 0.0, Avg: 0.1, Max: 0.2, Diff: 0.2, Sum: 1.0]
      [Update RS (ms): Min: 0.0, Avg: 0.4, Max: 0.4, Diff: 0.4, Sum: 2.8]
         [Processed Buffers: Min: 0, Avg: 4.8, Max: 6, Diff: 6, Sum: 38]
      [Scan RS (ms): Min: 0.0, Avg: 0.1, Max: 0.2, Diff: 0.2, Sum: 0.5]
      [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Object Copy (ms): Min: 5.0, Avg: 5.0, Max: 5.1, Diff: 0.1, Sum: 40.3]
      [Termination (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.1, Sum: 0.3]
         [Termination Attempts: Min: 1, Avg: 1.0, Max: 1, Diff: 0, Sum: 8]
      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
      [GC Worker Total (ms): Min: 5.1, Avg: 5.6, Max: 5.8, Diff: 0.7, Sum: 45.0]
      [GC Worker End (ms): Min: 1381.1, Avg: 1381.1, Max: 1381.1, Diff: 0.0]
   [Code Root Fixup: 0.0 ms]
   [Code Root Purge: 0.0 ms]
   [Clear CT: 0.1 ms]
   [Other: 0.6 ms]
      [Choose CSet: 0.0 ms]
      [Ref Proc: 0.2 ms]
      [Ref Enq: 0.0 ms]
      [Redirty Cards: 0.1 ms]
      [Humongous Register: 0.1 ms]
      [Humongous Reclaim: 0.1 ms]
      [Free CSet: 0.1 ms]
   [Eden: 17408.0K(17408.0K)->0.0B(17408.0K) Survivors: 3072.0K->3072.0K Heap: 319.3M(400.0M)->295.5M(400.0M)]
 [Times: user=0.06 sys=0.00, real=0.01 secs] 
```
整体与纯YoungGC基本一致， 其中能看到耗时明显变长，回收压缩的内存也更大。

### FullGC
```
2021-08-18T18:08:36.265+0800: [Full GC (Allocation Failure)  350M->267M(400M), 0.0477492 secs]
   [Eden: 0.0B(20480.0K)->0.0B(20480.0K) Survivors: 0.0B->0.0B Heap: 350.8M(400.0M)->267.8M(400.0M)], [Metaspace: 3255K->3255K(1056768K)]
 [Times: user=0.05 sys=0.00, real=0.05 secs] 
 ```
此次调用时将 `-Xmx`和`-Xms`均调整为`400m`才触发了FullGC，此前parallelGC在`-Xmx512m -Xmxs512m`时已触发多次FullGC，可以看出G1的内存回收性能还是比较好。
此次FullGC耗时47ms，并发标记后GC不及对象晋升速度以及生成速度时，可能触发FullGC。当发生了FullGC，内存回收时间可能会退化使用SerialGC，GC性能严重降低。

需要通过具体日志分析退化原因（如此次运行是内存不足导致，内存增加为`512M`则不再触发FullGC）