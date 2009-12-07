if false
  class IfCode
    class LocalBlock
      def initialize(p,s)
        assert{s}
        @parent=p
        @statement=s

        @blockName=tmpVar
      end

      def tmpVar
        @parent.tmpVar
      end

      def before
        c=@statement.before+
        @statement.code+
        @statement.after

        @parent.bgn(@blockName,["*"],c,@statement.value)
      end

      def value
        @blockName
      end

      def after
        s("clear #{@blockName}")
      end

      def s(str)
        @parent.s(str)
      end
    end

    def init
      @tmpVar=tmpVar
    end

    def before
      []
    end

    def code

      bcond=@cond
      bthen=@then
      belse=@else

      bthen=LocalBlock.new(self,bthen) if bthen
      belse=LocalBlock.new(self,belse) if belse

      #pp @else
      #exit

      before=bcond.before+bcond.code
      before+=bthen.before if bthen
      before+=belse.before if belse

      after=bcond.after
      after+=bthen.after if bthen
      after+=belse.after if belse

      val=bcond.value

      within=[]

      within+=s("if #{val} then #{@tmpVar}=#{bthen.value}()") if @then
      within+=s("unless #{val} then #{@tmpVar}=#{belse.value}()") if @else

      before+within+after
    end

    def value
      @tmpVar
    end

    def after
      s("clear #{@tmpVar}")
    end
  end

else
  class IfCode
    def init
      @valName=tmpVar
      @thenName=tmpVar
      @elseName=tmpVar
      @condVal=tmpVar

    end

    def before
      []
    end

    def code
      condCode=@cond.code+
      s("#{@condVal}=#{@cond.value}")

      result=condCode

      if @then
        result+=bgn(@thenName,["*"],@then.code+
        s("#{@valName}=#{@then.value}"),
        @then.value)
      end

      if @else
        result+=bgn(@elseName,["*"],@else.code+
        s("#{@valName}=#{@else.value}"),
        @else.value)
      end

      result+=s("if #{@condVal} then #{@thenName}") if @then
      result+=s("unless #{@condVal} then #{@elseName}") if @else

      result
    end

    def after
      s("clear #{@valName}")+
      s("clear #{@thenName}")+
      s("clear #{@elseName}")+
      s("clear #{@condVal}")
    end

    def value
      @valName
    end

  end
end