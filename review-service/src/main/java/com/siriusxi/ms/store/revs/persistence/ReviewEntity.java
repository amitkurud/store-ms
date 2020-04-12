package com.siriusxi.ms.store.revs.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(
    name = "reviews",
    indexes = ***REMOVED***
      @Index(name = "reviews_unique_idx", unique = true, columnList = "productId,reviewId")
***REMOVED***)
@Data
@NoArgsConstructor
public class ReviewEntity ***REMOVED***

  @Id @GeneratedValue private int id;

  @Version private int version;

  private int productId;
  private int reviewId;
  private String author;
  private String subject;
  private String content;

  public ReviewEntity(int productId, int reviewId, String author, String subject, String content) ***REMOVED***
    this.productId = productId;
    this.reviewId = reviewId;
    this.author = author;
    this.subject = subject;
    this.content = content;
***REMOVED***
***REMOVED***
