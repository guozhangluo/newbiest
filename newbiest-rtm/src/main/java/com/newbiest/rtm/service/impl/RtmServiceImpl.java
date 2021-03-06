package com.newbiest.rtm.service.impl;

import com.newbiest.base.exception.ClientException;
import com.newbiest.base.exception.ExceptionManager;
import com.newbiest.base.service.BaseService;
import com.newbiest.base.utils.CollectionUtils;
import com.newbiest.rtm.analyse.AnalyseContext;
import com.newbiest.rtm.analyse.DynaxAnalyse;
import com.newbiest.rtm.analyse.IAnalyse;
import com.newbiest.rtm.model.AnalyseResult;
import com.newbiest.rtm.model.DynaxAnalyseResult;
import com.newbiest.rtm.model.DynaxAnalyseResultDetail;
import com.newbiest.rtm.repository.DynaxAnalyseResultDetailRepository;
import com.newbiest.rtm.repository.DynaxAnalyseResultRepository;
import com.newbiest.rtm.service.RtmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guoxunbo on 2019/5/27.
 */
@Component
@Transactional
@Slf4j
public class RtmServiceImpl implements RtmService {

    @Autowired
    DynaxAnalyseResultRepository dynaxAnalyseResultRepository;

    @Autowired
    DynaxAnalyseResultDetailRepository dynaxAnalyseResultDetailRepository;

    @Autowired
    BaseService baseService;

    /**
     * 分析文件并产生结果保存
     */
    public void analyseFile(AnalyseContext analyseContext) throws ClientException{
        try {
            IAnalyse analyser = analyseContext.match();
            List<AnalyseResult> analyseResultList = analyser.analyse(analyseContext);
            if (analyser instanceof DynaxAnalyse) {
                deleteDynaxAnalyseResultByFileName(analyseContext.getFileName());
            }
            baseService.saveEntity(analyseResultList);
        } catch (Exception e) {
            throw ExceptionManager.handleException(e, log);
        }
    }

    /**
     * 根据文件名称删除已经解析过的结果
     */
    public void deleteDynaxAnalyseResultByFileName(String fileName) {
        try {
            List<DynaxAnalyseResult> fileResults = dynaxAnalyseResultRepository.getByFileName(fileName);
            if (CollectionUtils.isNotEmpty(fileResults)) {
                for (DynaxAnalyseResult result : fileResults) {
                    dynaxAnalyseResultDetailRepository.deleteByResultRrn(result.getObjectRrn());
                }
            }
            dynaxAnalyseResultRepository.deleteByFileName(fileName);
        } catch (Exception e) {
            throw ExceptionManager.handleException(e, log);
        }
    }

}
