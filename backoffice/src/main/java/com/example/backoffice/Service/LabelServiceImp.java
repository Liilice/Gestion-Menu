package com.example.backoffice.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backoffice.DTO.label.LabelDTO;
import com.example.backoffice.DTO.label.LabelResponseDTO;
import com.example.backoffice.Entity.Label;
import com.example.backoffice.Exception.notFoundException.LabelNotFoundException;
import com.example.backoffice.Mapper.LabelMapper;
import com.example.backoffice.Repository.LabelRepository;

@Service
public class LabelServiceImp implements LabelService {
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private LabelMapper labelMapper;

    public List<LabelResponseDTO> getAll() {
        return labelRepository.findAll().stream()
                .map(labelMapper::toResponseDTO)
                .toList();
    }

    public LabelResponseDTO create(LabelDTO payload) {
        Label label = new Label();
        label.setName(payload.getName().toLowerCase());
        return labelMapper.toResponseDTO(labelRepository.save(label));
    }

    public LabelResponseDTO getById(String id) throws LabelNotFoundException {
        Label label = getEntityById(id);
        return labelMapper.toResponseDTO(label);
    }

    @Transactional
    public LabelResponseDTO updateById(String id, LabelDTO payload) throws LabelNotFoundException {
        Label label = getEntityById(id);
        label.setName(payload.getName().toLowerCase());
        return labelMapper.toResponseDTO(labelRepository.save(label));
    }

    @Transactional
    public void deleteById(String id) throws LabelNotFoundException {
        Label label = getEntityById(id);
        labelRepository.delete(label);
    }

    public Label getEntityById(String id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new LabelNotFoundException(id));
    }
}
