package ru.alex.msdeal.mapper;


import java.util.List;


public interface Mappable<E, D> {

    E toEntity(D dto);

    D toDto(E entity);

    List<D> toDtoList(List<E> entities);

    List<E> toEntityList(List<D> entities);
}
