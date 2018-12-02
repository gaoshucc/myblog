package com.funnycode.myblog.mapper;

import com.funnycode.myblog.pojo.Note;
import com.funnycode.myblog.pojo.NoteType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author gaoshucc
 * @create 2018-11-29 19:34
 */
@Mapper
@Component("noteMapper")
public interface NoteMapper {
    /**
     * 根据userId获得手记
     * @param userId 用户id
     * @return Note 我的手记
     */
    List<Note> findNotesByUserId(String userId);
    /**
     * 根据userId获得手记
     * @param
     * @return Note 所有手记
     */
    List<Note> findAllNotes();
    /**
     * 根据noteId获得某一手记
     * @param noteId 手记Id
     * @return Note 手记
     */
    Note findNoteByNoteId(String noteId);
    /**
     * 根据noteId获得某一手记的内容
     * @param noteId 手记Id
     * @return String 手记内容
     */
    String findNoteHtmlByNoteId(String noteId);
    /**
     * 根据noteId获得某一手记的内容
     * @param
     * @return List<NoteType> 手记分类
     */
    List<NoteType> findNoteCate();
    /**
     * 根据noteId删除某一手记
     * @param noteId 手记的唯一标识
     * @return Integer 受影响的数据行数
     */
    Integer deleteNoteByNoteId(String noteId);
    /**
     * 根据noteId删除某一手记
     * @param userId 用户ID
     * @return List<Note> 手记列表
     */
    List<Note> findNoteRecycleBinByUserId(String userId);
    /**
     * 根据noteId还原某一手记
     * @param noteId 手记ID
     * @return Integer 受影响的数据行数
     */
    Integer recycleNoteByNoteId(String noteId);
    /**
     * 根据noteId彻底删除某一手记
     * @param noteId 手记ID
     * @return Integer 受影响的数据行数
     */
    Integer completelyDelNoteByNoteId(String noteId);
}
