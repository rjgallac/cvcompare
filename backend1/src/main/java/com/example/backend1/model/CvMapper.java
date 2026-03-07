package com.example.backend1.model;

public class CvMapper {

    public CurriculumVitae toEntity(CvDto cvDto) {
        CurriculumVitae curriculumVitae = new CurriculumVitae();
        curriculumVitae.setCurriculum_vitae_content(cvDto.getCurriculum_vitae_content());
        return curriculumVitae;
    }

    public CvDto toDto(CurriculumVitae curriculumVitae) {
        CvDto cvDto = new CvDto();
        cvDto.setCurriculum_vitae_content(curriculumVitae.getCurriculum_vitae_content());
        return cvDto;
    }

}
