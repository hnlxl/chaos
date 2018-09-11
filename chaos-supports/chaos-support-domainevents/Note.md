**Component、Entity、Repository都必须手动指定扫描这里，否则DDD支持部分无法使用**

    @ComponentScan(basePackageClasses = {SomeApplication.class, DomainEventsSupportConfiguration.class})
    @EntityScan(basePackageClasses = {SomeApplication.class, DomainEventsSupportConfiguration.class})
    @EnableJpaRepositories(basePackageClasses = {SomeApplication.class, DomainEventsSupportConfiguration.class})