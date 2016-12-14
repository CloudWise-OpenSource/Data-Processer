# Data-Processer
## 简介 
##### 1、是什么？
	
  他是一个模拟数据生成器。我们在测试过程中，产生完整、全面的真实数据可能比较困难。我们可以根据需求，创建对应的模版和词典，利用数据模拟生成器生成我们需要的模拟数据。

##### 2、能做什么？
	
  他能够根据构建的模版和词典，生成我们需要的数据。
	
##### 三个应用场景：

   - 测试场景
    
    测试过程中，我们需要验证数据后端的功能或性能，此时，需要降低与数据产生端的耦合，那么需要一个稳定优秀的数据生成器，来持续的不间断的产生正确的数据，和特殊情况下的异常数据。
    
   - 持续集成场景
    
    在整个持续集成场景中，一个或多个模块组成一个平台，需要有源源不断的数据进入持续集成环境，用以自动化地完成测试和迭代工作，使用Data-Processer则可以通过数据样本的指定和简单的偏码，非常简单地完成这个需求。
    
   - 生产场景
    
    在一个项目完成测试和迭代，发布到生产环境之后，通常也需要进行持续的功能或可用性监测，那么则需要有各种正常或异常数据按照某种规则和定义，持续稳定地生产并送回平台，此时将持续集成场景中的case，只需通过简单配置，则可以进行生产的验证，以满足这个需求。
		
##### 3、架构
    
  数据生成器包括：模版变量提取，模版变量执行，模版变量替换组成，三部分组成

##### 4、术语：
	
   * 函数变量：模版和词典中以"$Func{"开头，以"}"结尾的字符串是一个函数变量。形如：$Func{intRand()}，其中，intRand()为内置函数。不支持函数嵌套
   * 词典变量：模版中以"$Dic{"开头，以"}"结尾的字符串是一个词典变量。形如：$Dic{name},其中，name为词典文件中的一个词典名。
   * 自定义变量：模版中以"$Var{"开头，以"}"结尾的字符串是一个自定义变量。形如：$Var{tmp}，其中，tmp是自定义变量名。自定义变量需要与函数变量或者词典变量联合使用，中间以"="隔开，且无空格。
	  定义方式：$Var{tmp}=$Func{doubleRand(0,10,2)}。引用方式是；$Var{tmp}


##### 5、内置函数

   - long timestamp()
     生成当前13位时间戳（ms）。
   - int intRand()
     生成int正随机整数。
   - int intRand(Integer n)
     生成0～n的随机整数。
   - int intRand(Integer s, Integer e)
     生成s～e的随机整数。
   - long longRand()
     生成long正随机整数。
   - double doubleRand()
     生成0～1.0的随机双精度浮点数。
   - doubleRand(Integer s, Integer e, Integer n)
     生成s～e的保留n位有效数字的浮点数。
   - String uuid()
     生成一个uuid。
   - String numRand(Integer n)
     生成n位随机数。
   - String strRand(Integer n)
     生成n位字符串，包括数字，大小写字母。
    
##### 6、怎么用？
 
使用之前，需要引入simulatedata-generator的jar包。

（1） 编译：在simulatedata-generator目录下执行：

    $ mvn clean package
	
   在target目录下可以看到生成了simulatedata-generator-0.0.1-SNAPSHOT.jar文件。
   应用中引入此文件作为依赖即可。
   
（2） 编辑词典

    词典中含有模版中需要的常量以及函数，词典文件以dic为后缀名，test.dic词典文件形如:
        name=xiaoming|||hanmeimei|||lilei
        reqUrl=http://www.abc.com/a/b/c|||http://www.def.com/d/e/f
        tmp=$Func{intRand(1000000000, 1999999999)}
        b=testVar
        ot=$Func{intRand(2)}
  等号前面是词典名，等号后面是词典值，值可以是字符串，也可以是函数变量，多个值用"|||"隔开。如果有多个值，取值时，会随机取其中一个值作为词典变量值。
  `注意：词典文件放置在应用跟目录下的dictionaries目录下`。
		
（3） 编辑模版

    模版文件以tpl后缀名，test.tpl模版文件形如：
     {
        "infos":{
            "d_id": $Func{intRand()},
            "version": $Var{tmp}=$Func{doubleRand(0,10,2)},
            "os_type": "$Dic{ot}",
            "os": "$Var{tmp}",
            "test": "$Dic{b}"
            }
     }
  在模版文件中，在需要的地方放置变量（函数，词典，或者自定义），变量定义方式如第三点（术语）所述。在上面的模版中，version取值是$Func{doubleRand(0,10,2)}的值，然后将$Func{doubleRand(0,10,2)}的值赋给$Var{tmp}，在下面os处，以$Var{tmp}方式引用。这样，version和os的取值就一样了。
	  
（4） 调用Api执行
	    
     //加载词典
     DicInitializer.init();
     //创建模版分析器
     TemplateAnalyzer testTplAnalyzer = new TemplateAnalyzer(tplName, tpl);
     //分析模版生成模拟数据
     testTplAnalyzer.analyse();


## 使用示例

	 //加载词典(只需执行一次即可)
     DicInitializer.init();
	 //编辑模版
	 String tplName = "abc.tpl";
	 String tpl = "My name is $Dic{name}, my age is $Func{intRand(1,5)}";
	 //创建模版分析器（一个模版new一个TemplateAnalyzer对象即可）
	 TemplateAnalyzer testTplAnalyzer = new TemplateAnalyzer(tplName, tpl);
	 //分析模版生成模拟数据
	 String abc = testTplAnalyzer.analyse();
	 //打印分析结果
	 System.out.println(abc);

