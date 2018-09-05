**Component、Entity、Repository都必须手动指定扫描这里，否则DDD支持部分无法使用**

    @ComponentScan(basePackageClasses = {SomeApplication.class, JpaSupportConfiguration.class})
    @EntityScan(basePackageClasses = {SomeApplication.class, JpaSupportConfiguration.class})
    @EnableJpaRepositories(basePackageClasses = {SomeApplication.class, JpaSupportConfiguration.class})