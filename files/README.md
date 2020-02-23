之前如果使用的是lzyzsd的[JsBridge](https://github.com/lzyzsd/JsBridge)，如果不想做任何改造，只想解决丢失JS桥调用bug的问题，可以把这个js文件替换,
主要解决思路是创建多个iframe,在js运行环境中维护一个队列，最大iframe创建数量为10，可以根据自己项目对maxIframeCount进行调整，最大不要超过100，网页会报错；
