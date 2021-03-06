
-- ----------------------------
-- FUNCTION for _nextval
-- ----------------------------
CREATE FUNCTION _nextval(NAME VARCHAR(50))   
RETURNS INTEGER     
BEGIN    
DECLARE _CUR INT;  
DECLARE _MAXVALUE INT;  -- 接收最大值  
DECLARE _INCREMENT INT; -- 接收增长步数  

SET _CUR = (SELECT CURRENT_VAL FROM SYS_SEQUENCE WHERE SEQ_NAME = NAME);   
IF(_CUR is NULL) THEN
	INSERT INTO SYS_SEQUENCE (SEQ_NAME, MIN_VALUE, MAX_VALUE, CURRENT_VAL, INCREMENT_VAL) VALUES (NAME, 1, 99999999, 1, 1);
	SET _CUR = (SELECT CURRENT_VAL FROM SYS_SEQUENCE WHERE SEQ_NAME = NAME);   
END IF;
SET _INCREMENT = (SELECT INCREMENT_VAL FROM SYS_SEQUENCE WHERE SEQ_NAME = NAME);  
SET _MAXVALUE = (SELECT MAX_VALUE FROM SYS_SEQUENCE WHERE SEQ_NAME = NAME);  
 
UPDATE SYS_SEQUENCE                      -- 更新当前值  
 SET CURRENT_VAL = _CUR + INCREMENT_VAL    
 WHERE SEQ_NAME = NAME ;    
IF(_CUR + _INCREMENT >= _MAXVALUE) THEN  -- 判断是都达到最大值  
      UPDATE SYS_SEQUENCE    
        SET CURRENT_VAL = MIN_VALUE    
        WHERE SEQ_NAME = NAME ;  
END IF;  
RETURN _CUR;    
END

##split##;

-- ----------------------------
-- FUNCTION for _currval
-- ----------------------------
CREATE FUNCTION _currval(NAME VARCHAR(50)) 
RETURNS INTEGER
BEGIN    
DECLARE _CUR INT;  
DECLARE _INCREMENT INT; -- 接收增长步数  
SET _INCREMENT = (SELECT INCREMENT_VAL FROM SYS_SEQUENCE WHERE SEQ_NAME = NAME);  
SET _CUR = (SELECT CURRENT_VAL FROM SYS_SEQUENCE WHERE SEQ_NAME = NAME);    

RETURN _CUR-_INCREMENT;    
END