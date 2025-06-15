package cn.treedeep.king.generator.config;

import cn.treedeep.king.generator.model.*;
import cn.treedeep.king.shared.utils.Json5Parser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 模块配置JSON转换工具
 * <p>
 * 提供模块配置与JSON文件的相互转换功能
 *
 * @author 周广明
 * @since 2025-06-15
 */
@Slf4j
public class ModuleConfigConverter {

    private final ObjectMapper objectMapper;

    public ModuleConfigConverter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * 将模块列表转换为JSON字符串
     *
     * @param modules 模块列表
     * @return JSON字符串
     */
    public String modulesToJson(List<ModuleInfo> modules) {
        try {
            List<ModuleConfigDto> dtoList = modules.stream()
                    .map(this::moduleToDto)
                    .toList();
            return objectMapper.writeValueAsString(dtoList);
        } catch (Exception e) {
            log.error("转换模块为JSON失败", e);
            throw new RuntimeException("转换模块为JSON失败", e);
        }
    }

    /**
     * 将模块列表保存为JSON文件
     *
     * @param modules 模块列表
     * @param filePath 文件路径
     */
    public void saveModulesToJsonFile(List<ModuleInfo> modules, String filePath) {
        try {
            List<ModuleConfigDto> dtoList = modules.stream()
                    .map(this::moduleToDto)
                    .toList();

            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            objectMapper.writeValue(file, dtoList);
            log.info("模块配置已保存到文件: {}", filePath);
        } catch (IOException e) {
            log.error("保存模块配置到文件失败: {}", filePath, e);
            throw new RuntimeException("保存模块配置到文件失败", e);
        }
    }

    /**
     * 从JSON字符串加载模块列表
     *
     * @param json JSON字符串
     * @return 模块列表
     */
    public List<ModuleInfo> jsonToModules(String json) {
        try {
            ModuleConfigDto[] dtoArray = objectMapper.readValue(json, ModuleConfigDto[].class);
            return Stream.of(dtoArray)
                    .map(this::dtoToModule)
                    .toList();
        } catch (Exception e) {
            log.error("从JSON解析模块失败", e);
            throw new RuntimeException("从JSON解析模块失败", e);
        }
    }

    /**
     * 从JSON文件加载模块列表
     * <p>
     * 支持JSON5格式（包含注释和尾随逗号）
     *
     * @param filePath 文件路径
     * @return 模块列表
     */
    public List<ModuleInfo> loadModulesFromJsonFile(String filePath) {
        try {
            if (!Files.exists(Path.of(filePath))) {
                throw new RuntimeException("配置文件不存在: " + filePath);
            }

            // 读取文件内容
            String fileContent = Files.readString(Path.of(filePath));

            // 检测是否为JSON5格式并转换为标准JSON
            String processedJson;
            if (Json5Parser.isJson5(fileContent)) {
                log.info("📝 检测到JSON5格式配置文件，正在转换为标准JSON...");
                processedJson = Json5Parser.convertJson5ToJson(fileContent);
                log.info("✅ JSON5配置文件转换完成");
            } else {
                log.debug("📄 使用标准JSON格式配置文件");
                processedJson = fileContent;
            }

            // 解析JSON
            ModuleConfigDto[] dtoArray = objectMapper.readValue(processedJson, ModuleConfigDto[].class);
            List<ModuleInfo> modules = Stream.of(dtoArray)
                    .map(this::dtoToModule)
                    .toList();

            log.info("从文件加载了 {} 个模块配置: {}", modules.size(), filePath);
            return modules;
        } catch (IOException e) {
            log.error("从文件加载模块配置失败: {}", filePath, e);
            throw new RuntimeException("从文件加载模块配置失败", e);
        }
    }

    /**
     * 将Module转换为DTO
     */
    private ModuleConfigDto moduleToDto(ModuleInfo module) {
        ModuleConfigDto dto = new ModuleConfigDto();
        dto.setName(module.getName());
        dto.setComment(module.getComment());
        dto.setRemarks(module.getRemarks());

        // 转换聚合根
        dto.setAggregateRoots(module.getAggregateRoots().stream()
                .map(this::aggregateRootToDto)
                .toList());

        // 转换领域事件
        dto.setDomainEvents(module.getDomainEvents().stream()
                .map(this::domainEventToDto)
                .toList());

        // 转换应用服务
        dto.setApplicationServices(module.getApplicationServices().stream()
                .map(this::applicationServiceToDto)
                .toList());

        return dto;
    }

    /**
     * 将DTO转换为Module
     */
    private ModuleInfo dtoToModule(ModuleConfigDto dto) {
        // 转换聚合根
        List<AggregateRoot> aggregateRoots = new ArrayList<>();
        if (dto.getAggregateRoots() != null) {
            aggregateRoots = dto.getAggregateRoots().stream()
                    .map(this::dtoToAggregateRoot)
                    .toList();
        }

        // 转换领域事件
        List<DomainEvent> domainEvents = new ArrayList<>();
        if (dto.getDomainEvents() != null) {
            domainEvents = dto.getDomainEvents().stream()
                    .map(this::dtoToDomainEvent)
                    .toList();
        }

        // 转换应用服务
        List<ApplicationService> applicationServices = new ArrayList<>();
        if (dto.getApplicationServices() != null) {
            applicationServices = dto.getApplicationServices().stream()
                    .map(this::dtoToApplicationService)
                    .toList();
        }

        // 创建混合参数数组
        List<Object> items = new ArrayList<>();
        items.addAll(aggregateRoots);
        items.addAll(domainEvents);
        items.addAll(applicationServices);

        return ModuleInfo.create(dto.getName(), dto.getComment(), dto.getRemarks(), items.toArray());
    }

    /**
     * 聚合根转DTO
     */
    private ModuleConfigDto.AggregateRootDto aggregateRootToDto(AggregateRoot aggregateRoot) {
        ModuleConfigDto.AggregateRootDto dto = new ModuleConfigDto.AggregateRootDto();
        dto.setName(aggregateRoot.getName());
        dto.setComment(aggregateRoot.getComment());

        // 转换属性
        dto.setProperties(aggregateRoot.getProperties().stream()
                .map(this::propertyToDto)
                .toList());

        // 转换实体
        List<Entity> entities = aggregateRoot.getEos().stream()
                .filter(e -> !(e instanceof ValueObject))
                .toList();
        dto.setEntities(entities.stream()
                .map(this::entityToDto)
                .toList());

        // 转换值对象
        List<ValueObject> valueObjects = aggregateRoot.getEos().stream()
                .filter(e -> e instanceof ValueObject)
                .map(e -> (ValueObject) e)
                .toList();
        dto.setValueObjects(valueObjects.stream()
                .map(this::valueObjectToDto)
                .toList());

        // 转换方法
        dto.setMethods(aggregateRoot.getMethods().stream()
                .map(this::methodToDto)
                .toList());

        return dto;
    }

    /**
     * DTO转聚合根
     */
    private AggregateRoot dtoToAggregateRoot(ModuleConfigDto.AggregateRootDto dto) {
        List<Object> items = new ArrayList<>();

        // 添加属性
        if (dto.getProperties() != null) {
            dto.getProperties().forEach(propDto -> {
                items.add(dtoToProperty(propDto));
            });
        }

        // 添加实体
        if (dto.getEntities() != null) {
            dto.getEntities().forEach(entityDto -> {
                items.add(dtoToEntity(entityDto));
            });
        }

        // 添加值对象
        if (dto.getValueObjects() != null) {
            dto.getValueObjects().forEach(voDto -> {
                items.add(dtoToValueObject(voDto));
            });
        }

        // 添加方法
        if (dto.getMethods() != null) {
            dto.getMethods().forEach(methodDto -> {
                items.add(dtoToMethod(methodDto));
            });
        }

        return AggregateRoot.create(dto.getName(), dto.getComment(), items.toArray());
    }

    /**
     * 属性转换方法
     */
    private ModuleConfigDto.PropertyDto propertyToDto(Property property) {
        ModuleConfigDto.PropertyDto dto = new ModuleConfigDto.PropertyDto();
        dto.setName(property.getName());
        dto.setComment(property.getComment());

        if (Property.AggregateRootProperty.isAggregateRootProperty(property)) {
            dto.setType("AGGREGATE_ROOT_PROPERTY");
        } else if (Property.ValueObjectProperty.isValueObjectProperty(property)) {
            dto.setType("VALUE_OBJECT_PROPERTY");
        } else {
            dto.setType("REGULAR");
        }

        return dto;
    }

    private Property dtoToProperty(ModuleConfigDto.PropertyDto dto) {
        return switch (dto.getType()) {
            case "AGGREGATE_ROOT_PROPERTY" -> Property.AggregateRootProperty.create(dto.getName(), dto.getComment());
            case "VALUE_OBJECT_PROPERTY" -> Property.ValueObjectProperty.create(dto.getName(), dto.getComment());
            default -> Property.create(dto.getName(), dto.getComment());
        };
    }

    /**
     * 实体转换方法
     */
    private ModuleConfigDto.EntityDto entityToDto(Entity entity) {
        ModuleConfigDto.EntityDto dto = new ModuleConfigDto.EntityDto();
        dto.setName(entity.getName());
        dto.setComment(entity.getComment());
        dto.setProperties(entity.getProperties().stream()
                .map(this::propertyToDto)
                .toList());
        return dto;
    }

    private Entity dtoToEntity(ModuleConfigDto.EntityDto dto) {
        List<Property> properties = dto.getProperties().stream()
                .map(this::dtoToProperty)
                .toList();
        return Entity.create(dto.getName(), dto.getComment(), properties.toArray(new Property[0]));
    }

    /**
     * 值对象转换方法
     */
    private ModuleConfigDto.ValueObjectDto valueObjectToDto(ValueObject valueObject) {
        ModuleConfigDto.ValueObjectDto dto = new ModuleConfigDto.ValueObjectDto();
        dto.setName(valueObject.getName());
        dto.setComment(valueObject.getComment());
        dto.setProperties(valueObject.getProperties().stream()
                .map(this::propertyToDto)
                .toList());
        return dto;
    }

    private ValueObject dtoToValueObject(ModuleConfigDto.ValueObjectDto dto) {
        List<Property> properties = dto.getProperties().stream()
                .map(this::dtoToProperty)
                .toList();
        return ValueObject.create(dto.getName(), dto.getComment(), properties.toArray(new Property[0]));
    }

    /**
     * 方法转换方法
     */
    private ModuleConfigDto.MethodDto methodToDto(Method method) {
        ModuleConfigDto.MethodDto dto = new ModuleConfigDto.MethodDto();
        dto.setName(method.getName());
        dto.setComment(method.getComment());
        dto.setReturnType(method.getReturnType());
        dto.setParameters(method.getParameters().stream()
                .map(this::parameterToDto)
                .toList());
        return dto;
    }

    private Method dtoToMethod(ModuleConfigDto.MethodDto dto) {
        Method.Parameter[] parameters = dto.getParameters().stream()
                .map(this::dtoToParameter)
                .toArray(Method.Parameter[]::new);
        return Method.create(dto.getName(), dto.getComment(), dto.getReturnType(), parameters);
    }

    /**
     * 参数转换方法
     */
    private ModuleConfigDto.ParameterDto parameterToDto(Method.Parameter parameter) {
        ModuleConfigDto.ParameterDto dto = new ModuleConfigDto.ParameterDto();
        dto.setName(parameter.getName());
        dto.setType(parameter.getType());
        dto.setComment(parameter.getComment());
        return dto;
    }

    private Method.Parameter dtoToParameter(ModuleConfigDto.ParameterDto dto) {
        return Method.Parameter.create(dto.getName(), dto.getType(), dto.getComment());
    }

    /**
     * 领域事件转换方法
     */
    private ModuleConfigDto.DomainEventDto domainEventToDto(DomainEvent domainEvent) {
        ModuleConfigDto.DomainEventDto dto = new ModuleConfigDto.DomainEventDto();
        dto.setName(domainEvent.getName());
        dto.setComment(domainEvent.getComment());
        dto.setAggregateRootName(domainEvent.getAggregateRootName());
        dto.setTableName(domainEvent.getTableName());
        dto.setFields(domainEvent.getFields().stream()
                .map(this::eventFieldToDto)
                .toList());
        return dto;
    }

    private DomainEvent dtoToDomainEvent(ModuleConfigDto.DomainEventDto dto) {
        DomainEvent.EventField[] fields = dto.getFields().stream()
                .map(this::dtoToEventField)
                .toArray(DomainEvent.EventField[]::new);
        return DomainEvent.create(dto.getName(), dto.getComment(), dto.getAggregateRootName(), fields);
    }

    /**
     * 事件字段转换方法
     */
    private ModuleConfigDto.EventFieldDto eventFieldToDto(DomainEvent.EventField eventField) {
        ModuleConfigDto.EventFieldDto dto = new ModuleConfigDto.EventFieldDto();
        dto.setName(eventField.getName());
        dto.setType(eventField.getType());
        dto.setComment(eventField.getComment());
        dto.setColumnName(eventField.getColumnName());
        return dto;
    }

    private DomainEvent.EventField dtoToEventField(ModuleConfigDto.EventFieldDto dto) {
        return DomainEvent.EventField.create(dto.getName(), dto.getType(), dto.getComment(), dto.getColumnName());
    }

    /**
     * 应用服务转换方法
     */
    private ModuleConfigDto.ApplicationServiceDto applicationServiceToDto(ApplicationService applicationService) {
        ModuleConfigDto.ApplicationServiceDto dto = new ModuleConfigDto.ApplicationServiceDto();
        dto.setName(applicationService.getName());
        dto.setComment(applicationService.getComment());
        dto.setModuleName(applicationService.getModuleName());
        dto.setInterfaceName(applicationService.getInterfaceName());
        dto.setImplementationName(applicationService.getImplementationName());
        dto.setMethods(applicationService.getMethods().stream()
                .map(this::serviceMethodToDto)
                .toList());
        return dto;
    }

    private ApplicationService dtoToApplicationService(ModuleConfigDto.ApplicationServiceDto dto) {
        ApplicationService service = ApplicationService.create(dto.getName(), dto.getComment());

        if (dto.getMethods() != null) {
            dto.getMethods().forEach(methodDto -> {
                Method method = dtoToServiceMethod(methodDto);
                service.addMethod(method);
            });
        }

        return service;
    }

    /**
     * 服务方法转换方法
     */
    private ModuleConfigDto.ServiceMethodDto serviceMethodToDto(ApplicationService.ServiceMethod serviceMethod) {
        ModuleConfigDto.ServiceMethodDto dto = new ModuleConfigDto.ServiceMethodDto();
        dto.setName(serviceMethod.getName());
        dto.setComment(serviceMethod.getComment());
        dto.setReturnType(serviceMethod.getReturnType());
        dto.setParameters(serviceMethod.getParameters().stream()
                .map(this::serviceParameterToDto)
                .toList());
        return dto;
    }

    private Method dtoToServiceMethod(ModuleConfigDto.ServiceMethodDto dto) {
        Method.Parameter[] parameters = dto.getParameters().stream()
                .map(this::dtoToParameter)
                .toArray(Method.Parameter[]::new);
        return Method.create(dto.getName(), dto.getComment(), dto.getReturnType(), parameters);
    }

    /**
     * 服务参数转换方法
     */
    private ModuleConfigDto.ParameterDto serviceParameterToDto(ApplicationService.ServiceMethod.Parameter parameter) {
        ModuleConfigDto.ParameterDto dto = new ModuleConfigDto.ParameterDto();
        dto.setName(parameter.getName());
        dto.setType(parameter.getType());
        dto.setComment(parameter.getComment());
        return dto;
    }
}
