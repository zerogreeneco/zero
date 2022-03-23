package com.zerogreen.zerogreeneco.service.store;

import com.zerogreen.zerogreeneco.dto.store.StoreDto;
import com.zerogreen.zerogreeneco.repository.file.StoreImageFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoreImageServiceImpl implements StoreImageService{

    private final StoreImageFileRepository storeImageFileRepository;

    //Image List
    @Override
    public List<StoreDto> getImageByStore(Long sno) {
        return storeImageFileRepository.getImageByStore(sno).stream().map(StoreDto::new).collect(Collectors.toList());
    }

    @Override
    public void deleteImg(Long id, String filePath, String thumbFile) {
        File file = new File(filePath);
        File thumb = new File(thumbFile);
        try{
            if(file.exists()){
                boolean delete = file.delete();
                if(delete){
                    thumb.delete();
                    storeImageFileRepository.deleteById(id);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            throw  new IllegalStateException("파일 삭제 실패");
        }
    }
}
