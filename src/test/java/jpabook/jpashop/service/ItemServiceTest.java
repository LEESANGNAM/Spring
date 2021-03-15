package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.Movie;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {
    @Autowired ItemService itemService;
    @Autowired ItemRepository itemRepository;

        @Test
        @Rollback(value = false)
         public void 아이템생성() throws Exception{
             //given
            Book book = new Book();
            book.setAuthor("Lee");
            book.setName("jpa어려워용");
            book.setStockQuantity(3);


             //when성
            itemService.saveItem(book);

             //then
            assertEquals(book,itemRepository.findOne(book.getId()));

         }

        @Test
        @Rollback(value = false)
        public void 재고증가() throws Exception{
            //given
            Album album = new Album();
            album.setArtist("Sangnam");
            album.setName("학교가기 싫어용");
            album.setStockQuantity(2);
            //when
            itemService.saveItem(album);
            album.addStock(2);
            //then
            assertEquals(album,itemRepository.findOne(album.getId()));
        }

        @Test
        @Rollback(value = false)
        public void 재고감소() throws Exception{
            //given
            Album album = new Album();
            album.setArtist("Sangnam");
            album.setName("집에가고 싶어요");
            album.setStockQuantity(10);
            //when
            itemService.saveItem(album);
            album.removeStock(4);
            //then
            assertEquals(album,itemRepository.findOne(album.getId()));
        }
}