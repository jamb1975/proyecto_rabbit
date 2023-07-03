package co.com.bancolombia.dynamodb.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class MapperConfig<E, D> {

    private final Class<E> entity;
    private final Class<D> data;

    private final ModelMapper mapper;

    public MapperConfig(Class<E> entity, Class<D> data) {
        this.data = data;
        this.entity = entity;

        mapper = new ModelMapper();
        var configuration = mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD);
        mapper.createTypeMap(data, entity, configuration);
    }
}
