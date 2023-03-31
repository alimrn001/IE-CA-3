package com.baloot.IE_CA_3.Baloot.Comment;

import com.baloot.IE_CA_3.Baloot.Exceptions.CommentNotExistsException;
import com.baloot.IE_CA_3.Baloot.Exceptions.CommodityNotExistsException;
import com.baloot.IE_CA_3.Baloot.Exceptions.WrongVoteValueException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CommentsManager {

    private final Map<Integer, Comment> balootComments = new HashMap<>();

    private int latestCommentID = 0; //comments id start with 1


    public boolean commentExists(int commentId) {
        return balootComments.containsKey(commentId);
    }

    public void addComment(Comment comment) {
        comment.setCommentId(latestCommentID+1);
        comment.setLikesNo(0);
        comment.setDislikesNo(0);
        balootComments.put(comment.getCommentId(), comment);
        latestCommentID++;
    }

    public void addCommentByUserInput(String username, int commodityId, String text) {
        Comment comment = new Comment
                (latestCommentID+1, username, commodityId, text, LocalDate.now().toString());
        balootComments.put(comment.getCommentId(), comment);
        latestCommentID++;
    }

    public void voteComment(int commentId, int vote, boolean beenLikedBefore, boolean beenDislikedBefore) throws Exception {
        if(vote == 1) {
            if(!beenLikedBefore)
                balootComments.get(commentId).addLike();
            if(beenDislikedBefore)
                balootComments.get(commentId).removeDislike();
        }
        else if(vote == -1) {
            if(!beenDislikedBefore)
                balootComments.get(commentId).addDislike();
            if(beenLikedBefore)
                balootComments.get(commentId).removeLike();
        }
        else
            throw new WrongVoteValueException();

    }

    public Map<Integer, Comment> getBalootComments() {
        return this.balootComments;
    }

    public Comment getBalootComment(int commentId) throws Exception {
        if(!commentExists(commentId))
            throw new CommentNotExistsException();
        return balootComments.get(commentId);
    }

    public Map<Integer, Comment> getCommodityComments(int commodityId) {
        Map<Integer, Comment> result = new HashMap<>();
        for (Map.Entry<Integer, Comment> commentEntry : balootComments.entrySet()) {
            if(commentEntry.getValue().getCommodityId()==commodityId)
                result.put(commentEntry.getKey(), commentEntry.getValue());
        }
        return result;
    }

}
