package com.funnycode.myblog.service;

import com.funnycode.myblog.pojo.PO.Comment;
import com.funnycode.myblog.pojo.PO.Note;
import com.funnycode.myblog.pojo.PO.NoteType;

import java.util.List;

/**
 * @author gaoshucc
 * @create 2018-11-30 17:06
 */
public interface NoteService {

    List<Note> findMyNotesByUserId(String userId);

    List<Note> findAllNotes();

    Note findNoteByNoteId(String noteId);

    String findNoteHtmlByNoteId(String noteId);

    List<NoteType> findNoteCate();

    boolean deleteNoteByNoteId(String noteId);

    List<Note> findNoteRecycleBinByUserId(String userId);

    boolean recycleNoteByNoteId(String noteId);

    boolean completelyDelNoteByNoteId(String noteId);

    boolean saveComment(Comment comment);

    List<Comment> findCommends(String noteId);

    List<Note> findAllNotesLimit(Integer start, Integer end);

    Integer findLikeCount(String noteId);

    Integer findNoteCountByAuthorId(String authorId);
}
