package com.funnycode.myblog.service.impl;

import com.funnycode.myblog.mapper.NoteMapper;
import com.funnycode.myblog.pojo.PO.Comment;
import com.funnycode.myblog.pojo.PO.Note;
import com.funnycode.myblog.pojo.PO.NoteType;
import com.funnycode.myblog.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author gaoshucc
 * @create 2018-11-30 17:07
 */
@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteMapper noteMapper;

    @Override
    public List<Note> findMyNotesByUserId(String userId) {
        return noteMapper.findNotesByUserId(userId);
    }

    @Override
    public List<Note> findAllNotes() {
        return noteMapper.findAllNotes();
    }

    @Override
    public Note findNoteByNoteId(String noteId) {
        return noteMapper.findNoteByNoteId(noteId);
    }

    @Override
    public String findNoteHtmlByNoteId(String noteId) {
        return noteMapper.findNoteHtmlByNoteId(noteId);
    }

    @Override
    public List<NoteType> findNoteCate() {
        return noteMapper.findNoteCate();
    }

    @Override
    public boolean deleteNoteByNoteId(String noteId) {
        Integer delSuccess = noteMapper.deleteNoteByNoteId(noteId);

        return delSuccess>0?true:false;
    }

    @Override
    public List<Note> findNoteRecycleBinByUserId(String userId) {
        return noteMapper.findNoteRecycleBinByUserId(userId);
    }

    @Override
    public boolean recycleNoteByNoteId(String noteId) {
        Integer recycleSuccess = noteMapper.recycleNoteByNoteId(noteId);

        return recycleSuccess>0?true:false;
    }

    @Override
    public boolean completelyDelNoteByNoteId(String noteId) {
        Integer completelyDelSuccess = noteMapper.completelyDelNoteByNoteId(noteId);

        return completelyDelSuccess>0?true:false;
    }

    @Override
    public boolean saveComment(Comment comment) {
        Integer saveCommentSuccess = noteMapper.saveComment(comment);

        return saveCommentSuccess>0?true:false;
    }

    @Override
    public List<Comment> findCommends(String noteId) {
        return noteMapper.findCommentsByNoteId(noteId);
    }

    @Override
    public List<Note> findAllNotesLimit(Integer start, Integer end) {
        return noteMapper.findAllNotesLimit(start, end);
    }

    @Override
    public Integer findLikeCount(String noteId) {
        return noteMapper.findLikeCountByNoteId(noteId);
    }

    @Override
    public Integer findNoteCountByAuthorId(String authorId) {
        return noteMapper.findNoteCountByAuthorId(authorId);
    }

    @Override
    public List<Note> findNotesNoteTypeId(Integer noteTypeId) {
        return noteMapper.findNotesNoteTypeId(noteTypeId);
    }

}
