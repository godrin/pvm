class IasgnCode
  def before
    []
  end

  def after
    []
  end

  def code
    if @value.nil?
      s(@name.to_s)
    else
      res=@value.before+@value.code+s("#{@name}=#{@value.value}")+@value.after
    end
  end

  def value
    @name
  end
end