package com.github.mathieus.agencyapp.dto;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
public class PageResponse<T> extends PageImpl<T> {

    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int number; // page number
    private int size;   // page size

    public PageResponse(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PageResponse(List<T> content) {
        super(content);
    }
    public PageResponse(){
        super(new ArrayList<>());
    }

    // getters & setters
    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
}