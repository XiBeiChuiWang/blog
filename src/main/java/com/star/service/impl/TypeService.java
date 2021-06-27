package com.star.service.impl;

import com.star.domain.Type;
import com.star.mapper.TypeMapper;
import com.star.service.ITypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TypeService implements ITypeService {

    @Autowired
    TypeMapper typeMapper;

    @Override
    public int saveType(Type type) {
        return typeMapper.saveType(type);
    }

    @Override
    public Type getType(Long id) {
        return typeMapper.getType(id);
    }

    @Override
    public List<Type> getAllType() {
        return typeMapper.getAllType();
    }

    @Override
    public Type getTypeByName(String name) {
        return typeMapper.getTypeByName(name);
    }

    @Override
    public int updateType(Type type) {
        return typeMapper.updateType(type);
    }

    @Override
    public void deleteType(Long id) {
        typeMapper.deleteType(id);
    }

    @Transactional
    @Override
    public List<Type> getAllTypeAndBlog() {
        return typeMapper.getAllTypeAndBlog();
    }
}
