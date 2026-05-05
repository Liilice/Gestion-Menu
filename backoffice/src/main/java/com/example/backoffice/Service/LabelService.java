package com.example.backoffice.Service;

import java.util.List;

import com.example.backoffice.DTO.label.LabelDTO;
import com.example.backoffice.DTO.label.LabelResponseDTO;
import com.example.backoffice.Entity.Label;
import com.example.backoffice.Exception.notFoundException.LabelNotFoundException;


public interface LabelService {
    List<LabelResponseDTO> getAll();
    LabelResponseDTO create(LabelDTO payload);
    LabelResponseDTO getById(String id) throws LabelNotFoundException;
    LabelResponseDTO updateById(String id, LabelDTO payload) throws LabelNotFoundException;
    void deleteById(String id) throws LabelNotFoundException;
    Label getEntityById(String id);
} 