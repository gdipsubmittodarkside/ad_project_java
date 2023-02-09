package nus.iss.team2.ADProjectTECHS.Service.Impl;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import nus.iss.team2.ADProjectTECHS.Model.CourseCrawled;
import nus.iss.team2.ADProjectTECHS.Model.Skill;
import nus.iss.team2.ADProjectTECHS.Repository.CourseCrawledRepository;
import nus.iss.team2.ADProjectTECHS.Service.CourseCrawledService;

@Service
public class CourseCrawledServiceImpl implements CourseCrawledService {
    
    @Autowired
    private CourseCrawledRepository courseCrawledRepository;
    @Override
    public List<CourseCrawled> findCoursesSortedBySubscribers() {
        return courseCrawledRepository.findCoursesSortedBySubscribers();

    }

    @Override
    public List<CourseCrawled> findCoursesTitleLike(String title) {
        return courseCrawledRepository.findCourseCrawledsByCourseTitleContainsIgnoreCase(title);
    }

    @Override
    public List<CourseCrawled> findCoursesBySkill(Skill skill) {
        return courseCrawledRepository.findCoursesBySkill(skill);
    }

    @Override
    public List<CourseCrawled> findCoursesBySkillTitleLike(String skillTitle) {
        return courseCrawledRepository.findCoursesCrawledBySkillTitleContains(skillTitle);
    }

    @Override
    public List<CourseCrawled> findCoursesSortedByViews() {
        return courseCrawledRepository.findCoursesSortedByViews();
    }

    @Override
    public void cancelForeignKeyConstraint() {
        courseCrawledRepository.cancelForeignKeyConstraint();
    }


    @Override
    public void enableForeignKeyConstraint() {
        courseCrawledRepository.enableForeignKeyConstraint();
    }

    @Override
    public void truncateTable() {
        courseCrawledRepository.truncateTable();
    }

    @Override
    public List<CourseCrawled> getCourseCrawledList(){
        return courseCrawledRepository.findAll();
    }

    @Override
    public CourseCrawled saveCourseCrawled(CourseCrawled courseCrawled){
        return courseCrawledRepository.save(courseCrawled);
    }

    @Override
    public Optional<CourseCrawled> findCourseCrawled(long id){
        return courseCrawledRepository.findById(id);
    }

    @Override
    public CourseCrawled findCourseCrawledById(long id){
        return courseCrawledRepository.findCourseCrawledByCourseId(id);
    }

    @Override
    @Transactional
    public CourseCrawled updateCourseCrawled(CourseCrawled courseCrawled){
        return courseCrawledRepository.save(courseCrawled);
    }

    @Override
    public Boolean deleteCourseCrawledById(Long id){

        try{
            courseCrawledRepository.deleteById(id);
            return true;
        }

        catch(Exception e){
            return false;
        }
    }

    @Override
    public Page<CourseCrawled> findPaginated(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(1, 5);
        return courseCrawledRepository.findAll(pageable);
    }

    @Override
    public List<CourseCrawled> findCoursesBySkillId(long skillId) {
        return courseCrawledRepository.findCourseBySkillId(skillId);
    }

    @Override
    public CourseCrawled findCourseCrawledByUrl(String url){
        return courseCrawledRepository.findCourseCrawledByUrl(url);
    }




}
